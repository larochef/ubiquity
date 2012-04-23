/**
 * 
 */
package org.ubiquity.bytecode;

import org.objectweb.asm.*;
import org.ubiquity.Copier;
import org.ubiquity.util.Tuple;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;
import static org.ubiquity.bytecode.GeneratorHelper.*;
import static org.ubiquity.util.Constants.SIMPLE_PROPERTIES;

/**
 *
 * TODO : when copying arrays / list, don't expect the order to exactly match, find a way to configure matching !!!
 *
 * @author François LAROCHE
 *
 */
final class CopierGenerator {

    private Class<? extends List> defaultListImplementation;
    private Class<? extends Set> defaultSetImplementation;
    private Class<? extends Map> defaultMapImplementation;
	
	CopierGenerator() {
        this.defaultListImplementation = ArrayList.class;
        this.defaultSetImplementation = HashSet.class;
        this.defaultMapImplementation = HashMap.class;
    }

    private final MyClassLoader loader = new MyClassLoader();

	public Map<String, Property> findProperties(Class<?> clazz) {
		try {
			ClassReader reader = new ClassReader(byteCodeName(clazz));
			PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
			reader.accept(visitor, 0);
			return visitor.getProperties();
		} catch (IOException e) {
			throw new IllegalStateException("Unable to parse class : ", e);
		}
	}

	public <T, U> Copier<T, U> createCopier(Class<T> src, Class<U> destination, CopyContext ctx)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<Tuple<Property, Property>> properties = listCompatibelProperties(src, destination);
        String srcName = byteCodeName(src);
        String destinationName = byteCodeName(destination);
        String className = "org/ubiquity/bytecode/generated/Copier" + src.getSimpleName() + destination.getSimpleName();

        ClassWriter writer = new ClassWriter(0);
        writer.visit(V1_5, ACC_PUBLIC + ACC_FINAL, className,
                "Lorg/ubiquity/bytecode/SimpleCopier<" + getDescription(srcName) + getDescription(destinationName) + ">;",
                "org/ubiquity/bytecode/SimpleCopier", null);

        createConstructor(writer, className);
        createNewInstance(writer, className, destinationName);
        createCopyBridge(writer, className, srcName, destinationName);
        createNewArray(writer, className, destinationName);

        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC + ACC_FINAL, "copy", '(' + getDescription(srcName) + getDescription(destinationName) + ")V", null, null);
        for(Tuple<Property, Property> p : properties) {
            String descriptionGetter = getDescription(p.tObject.getTypeGetter());
            String descriptionSetter = getDescription(p.uObject.getTypeSetter());
            // Handle simple properties, like String, Integer
            if(SIMPLE_PROPERTIES.containsKey(descriptionGetter)
                    && (SIMPLE_PROPERTIES.get(descriptionGetter).equals(descriptionSetter)
                    || descriptionGetter.equals(descriptionSetter))) {
                visitor.visitVarInsn(ALOAD, 2);
                if(!descriptionGetter.equals(descriptionSetter)) {
                    visitor.visitVarInsn(ALOAD, 0);
                }
                visitor.visitVarInsn(ALOAD, 1);
                visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
                if(!descriptionGetter.equals(descriptionSetter)) {
                    visitor.visitMethodInsn(INVOKEVIRTUAL, className, "convert", "(" + descriptionGetter + ")" + descriptionSetter);
                }
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
                continue;
            }
            // Handle complex properties, ie possibly needing conversion
            // Handle arrays
            if(descriptionGetter.startsWith("[")) {
                // TODO : handle simple properties
                String typeDescriptionGetter = descriptionGetter.substring(1);
                if(SIMPLE_PROPERTIES.containsKey(typeDescriptionGetter)) {
                    // handle simple arrays
                    String typeDescriptionSetter = descriptionSetter.substring(1);
                    continue;
                }
                // TODO : correct.me : retrieve correct copier from the context, and require it.
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitVarInsn(ALOAD, 0);
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitVarInsn(ALOAD, 1);
                visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()" + getDescription(p.uObject.getTypeGetter()));
                visitor.visitMethodInsn(INVOKEVIRTUAL, className, "map", "([" + getDescription(srcName) + ",[" +  getDescription(p.uObject.getTypeGetter())
                        + ")[" + getDescription(p.uObject.getTypeGetter()));
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
                continue;
            }
            if("Ljava/util/List;".equals(descriptionGetter)) {
                continue;
            }
            if("Ljava/util/Set;".equals(descriptionGetter)) {
                continue;
            }
            if("Ljava/util/Map;".equals(descriptionGetter)) {
                continue;
            }

            handleComplexObjects(visitor, className, srcName, destinationName, p);

            // case of objects
            // Get the U.class
//                visitor.visitLdcInsn(Type.getType(descriptionSetter));
            // TODO : copy object, or map it if null !
            // TODO : handle collections
        }
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(4,4);
        visitor.visitEnd();

        writer.visitEnd();

        Class<?> resultClass = loader.defineClass(className.replaceAll("[/]", "."), writer.toByteArray());
        @SuppressWarnings("unchecked")
        Copier<T,U> instance =  (Copier<T,U>) resultClass.getConstructor(CopyContext.class).newInstance(ctx);
        ctx.registerCopier(src, destination, instance);
        ctx.createRequiredCopiers();
        return instance;
	}

    private List<Tuple<Property, Property>> listCompatibelProperties(Class<?> source, Class<?> destination) {
        List<Tuple<Property, Property>> compatibleProperties = new ArrayList<Tuple<Property, Property>>();
        Map<String, Property> srcProperties = findProperties(source);
        Map<String, Property> targetProperties = findProperties(destination);

        for(String name : srcProperties.keySet()) {
            Property property = srcProperties.get(name);
            if(!property.isReadable()) {
                continue;
            }
            Property dest = resolveTargetProperty(property, targetProperties);
            if(dest != null && dest.isWritable()) {
                compatibleProperties.add(new Tuple<Property, Property>(property, dest));
            }
        }

        return compatibleProperties;
    }

    private Property resolveTargetProperty(Property src, Map<String, Property> targetProperties) {
        return targetProperties.get(src.getName());
    }

	private static String byteCodeName(Class<?> c) {
		return c.getName().replaceAll("[\\.]", "/");
	}

    private static class MyClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            Class result = this.findLoadedClass(name);
            if(result == null) {
                result = defineClass(name, b, 0, b.length);
            }
            return result;
        }
    }

    public Class<? extends List> getDefaultListImplementation() {
        return defaultListImplementation;
    }

    public void setDefaultListImplementation(Class<? extends List> defaultListImplementation) {
        if(Modifier.isAbstract(defaultListImplementation.getModifiers()) || defaultListImplementation.isInterface()) {
            throw new IllegalArgumentException("The given class cannot create lists ! (" + defaultListImplementation.getName() + ")");
        }
        this.defaultListImplementation = defaultListImplementation;
    }

    public Class<? extends Set> getDefaultSetImplementation() {
        return defaultSetImplementation;
    }

    public void setDefaultSetImplementation(Class<? extends Set> defaultSetImplementation) {
        if(Modifier.isAbstract(defaultSetImplementation.getModifiers()) || defaultSetImplementation.isInterface()) {
            throw new IllegalArgumentException("The given class cannot create sets ! (" + defaultSetImplementation.getName() + ")");
        }
        this.defaultSetImplementation = defaultSetImplementation;
    }

    public Class<? extends Map> getDefaultMapImplementation() {
        return defaultMapImplementation;
    }

    public void setDefaultMapImplementation(Class<? extends Map> defaultMapImplementation) {
        if(Modifier.isAbstract(defaultMapImplementation.getModifiers()) || defaultMapImplementation.isInterface()) {
            throw new IllegalArgumentException("The given class cannot create maps ! (" + defaultMapImplementation.getName() + ")");
        }
        this.defaultMapImplementation = defaultMapImplementation;
    }
}
