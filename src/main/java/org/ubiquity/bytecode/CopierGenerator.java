/**
 * 
 */
package org.ubiquity.bytecode;

import org.objectweb.asm.*;
import org.ubiquity.Copier;
import org.ubiquity.util.Constants;
import org.ubiquity.util.Tuple;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;
import static org.ubiquity.bytecode.GeneratorHelper.*;
import static org.ubiquity.util.Constants.COLLECTIONS;
import static org.ubiquity.util.Constants.SIMPLE_PROPERTIES;

/**
 *
 * TODO : when copying arrays / list, don't expect the order to exactly match, find a way to configure matching !!!
 *
 * @author François LAROCHE
 *
 */
final class CopierGenerator {

	CopierGenerator() {}

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
        String className = createCopierClassName(srcName, destinationName);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
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

            // Handle arrays
            if(descriptionGetter.startsWith("[")) {
                handeArrays(visitor, className, srcName, destinationName, p);
                continue;
            }
            // Handle collections
            if(COLLECTIONS.contains(descriptionGetter)) {
                String type = descriptionGetter.substring(
                                descriptionGetter.lastIndexOf("/") + 1,
                                descriptionGetter.length() - 1);
                handleCollection(visitor, p, type, srcName, destinationName);
                continue;
            }
            // Handle other objects
            handleComplexObjects(visitor, className, srcName, destinationName, p);
        }
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(7,4);
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
            Property dest = resolveTargetProperty(property, targetProperties,
                    getDescription(CopierGenerator.byteCodeName(source)), getDescription(CopierGenerator.byteCodeName(destination)));
            if(dest != null && dest.isWritable()) {
                compatibleProperties.add(new Tuple<Property, Property>(property, dest));
            }
        }

        return compatibleProperties;
    }

    private Property resolveTargetProperty(Property src, Map<String, Property> targetProperties, String srcDescription, String destinationDescription) {
        String sourceName = src.getName();
        String matchingAnnotation = null;
        for(String annotation : src.getAnnotations()) {
            if(annotation.startsWith(Constants.RENAME_ANNOTATION) && (annotation.endsWith(destinationDescription) || annotation.endsWith("*"))) {
                if(matchingAnnotation == null || annotation.endsWith(destinationDescription)) {
                    matchingAnnotation = annotation;
                }
            }
        }
        if(matchingAnnotation != null) {
            sourceName = matchingAnnotation.split("[:]")[1];
        }

        for(String key : targetProperties.keySet()) {
            Property p = targetProperties.get(key);
            if(sourceName.equals(key)) {
                return p;
            }
            String annotationBeginning = Constants.RENAME_ANNOTATION + ":" + sourceName + ":";
            if(p.getAnnotations().contains(annotationBeginning + "*") || p.getAnnotations().contains(annotationBeginning + srcDescription)) {
                return p;
            }
        }
        return null;
    }

	private static String byteCodeName(Class<?> c) {
		return c.getName().replaceAll("[\\.]", "/");
	}

    private static class MyClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
