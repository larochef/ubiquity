/**
 * 
 */
package org.ubiquity.bytecode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.*;
import org.ubiquity.Copier;
import org.ubiquity.util.Tuple;

import static  org.objectweb.asm.Opcodes.*;

/**
 *
 * TODO : when copying arrays / list, don't expect the order to exactly match, find a way to configure matching !!!
 *
 * @author François LAROCHE
 *
 */
class CopierGenerator {

    private static final Map<String, String> SIMPLE_PROPERTIES = new HashMap<String, String>();
    static {
        SIMPLE_PROPERTIES.put("Z", "Ljava/lang/Boolean;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Boolean;", "Z");
        SIMPLE_PROPERTIES.put("C", "Ljava/lang/Character;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Character;", "C");
        SIMPLE_PROPERTIES.put("B", "Ljava/lang/Byte;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Byte;", "B");
        SIMPLE_PROPERTIES.put("S", "Ljava/lang/Short;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Short;", "S");
        SIMPLE_PROPERTIES.put("I", "Ljava/lang/Integer;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Integer;", "I");
        SIMPLE_PROPERTIES.put("F", "Ljava/lang/Float;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Floats;", "F");
        SIMPLE_PROPERTIES.put("J", "Ljava/lang/Long;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Long;", "J");
        SIMPLE_PROPERTIES.put("D", "Ljava/lang/Double;");
        SIMPLE_PROPERTIES.put("Ljava/lang/Double;", "D");
        SIMPLE_PROPERTIES.put("Ljava/lang/String;", "Ljava/lang/String;");
    }
	
	private CopierGenerator() {}

    private static final MyClassLoader loader = new MyClassLoader();

	public static Map<String, Property> findProperties(Class<?> clazz) {
		try {
			ClassReader reader = new ClassReader(byteCodeName(clazz));
			PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
			reader.accept(visitor, 0);
			return visitor.getProperties();
		} catch (IOException e) {
			throw new IllegalStateException("Unable to parse class : ", e);
		}
	}

    private static void createConstructor(ClassWriter writer, String className) {
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/ubiquity/bytecode/CopyContext;)V", null, null);
        Label label1 = new Label();
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/bytecode/SimpleCopier", "<init>", "(Lorg/ubiquity/bytecode/CopyContext;)V");
        Label label2 = new Label();
        visitor.visitLabel(label2);
        visitor.visitInsn(RETURN);
        Label label3 = new Label();
        visitor.visitLabel(label3);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label3, 0);
        visitor.visitLocalVariable("context", "Lorg/ubiquity/bytecode/CopyContext;", null, label1, label3, 1);
        visitor.visitMaxs(2, 2);
        visitor.visitEnd();
    }

    private static  void createNewInstance(ClassWriter writer, String className, String destinationName) {
        Label label1 = new Label();
        Label label2 = new Label();
        MethodVisitor visitor = writer.visitMethod(ACC_PROTECTED + ACC_VOLATILE + ACC_BRIDGE, "newInstance", "()Ljava/lang/Object;", null, null);
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKEVIRTUAL, className, "newInstance", "()" + getDescription(destinationName));
        visitor.visitInsn(ARETURN);
        visitor.visitLabel(label2);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label2, 0);
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();

        label1 = new Label();
        label2 = new Label();
        visitor = writer.visitMethod(ACC_PROTECTED, "newInstance", "()" + getDescription(destinationName), null, null);
        visitor.visitLabel(label1);
        visitor.visitTypeInsn(NEW, destinationName);
        visitor.visitInsn(DUP);
        visitor.visitMethodInsn(INVOKESPECIAL, destinationName, "<init>", "()V");
        visitor.visitInsn(ARETURN);
        visitor.visitLabel(label2);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label2, 0);
        visitor.visitMaxs(2, 1);
        visitor.visitEnd();
    }

    private static void createNewArray(ClassWriter writer, String className, String destinationName) {
        MethodVisitor visitor = writer.visitMethod(ACC_PROTECTED + ACC_VOLATILE + ACC_BRIDGE, "newArray", "(I)[Ljava/lang/Object;", null, null);
        Label label1 = new Label();
        Label label2 = new Label();
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitVarInsn(ILOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, className, "newArray", "(I)[" + getDescription(destinationName));
        visitor.visitInsn(ARETURN);
        visitor.visitLabel(label2);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label2, 0);
        visitor.visitLocalVariable("x0", "I", null, label1, label2, 1);
        visitor.visitMaxs(2, 2);
        visitor.visitEnd();

        visitor = writer.visitMethod(ACC_PROTECTED, "newArray", "(I)[" + getDescription(destinationName), null, null);
        label1 = new Label();
        label2 = new Label();
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ILOAD, 1);
        visitor.visitTypeInsn(ANEWARRAY, destinationName);
        visitor.visitInsn(ARETURN);
        visitor.visitLabel(label2);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label2, 0);
        visitor.visitLocalVariable("capacity", "I", null, label1, label2, 1);
        visitor.visitMaxs(1, 2);
        visitor.visitEnd();
    }

    private static  void createCopyBridge(ClassWriter writer, String className, String srcName, String destinationName) {
        Label label1 = new Label();
        Label label2 = new Label();
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC + ACC_VOLATILE + ACC_BRIDGE, "copy", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitTypeInsn(CHECKCAST, srcName);
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitTypeInsn(CHECKCAST, destinationName);
        visitor.visitMethodInsn(INVOKEVIRTUAL, className, "copy", '(' + getDescription(srcName) + getDescription(destinationName) + ")V");
        visitor.visitInsn(RETURN);
        visitor.visitLabel(label2);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label2, 0);
        visitor.visitLocalVariable("x0", "Ljava/lang/Object;", null, label1, label2, 1);
        visitor.visitLocalVariable("x1", "Ljava/lang/Object;", null, label1, label2, 2);
        visitor.visitMaxs(3, 3);
        visitor.visitEnd();
    }
	
	public static <T, U> Copier<T, U> createCopier(Class<T> src, Class<U> destination, CopyContext ctx)
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
                visitor.visitVarInsn(ALOAD, 1);
                visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
                if(!descriptionGetter.equals(descriptionSetter)) {
                    visitor.visitVarInsn(ALOAD, 0);
                    visitor.visitMethodInsn(INVOKEVIRTUAL, className, "convert", "(" + descriptionGetter + ")" + descriptionSetter);
                }
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
            }
            // Handle complex properties, ie possibly needing conversion
            else {
                // Load converter
                // Handle cas of arrays
                if(descriptionGetter.startsWith("[")) {
                    // TODO : handle the case where array isn't empty
                    visitor.visitVarInsn(ALOAD, 2);
                    visitor.visitVarInsn(ALOAD, 0);
                    visitor.visitVarInsn(ALOAD, 1);
                    visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
                    visitor.visitMethodInsn(INVOKEVIRTUAL, className, "map", "([" + getDescription(srcName) + ",[" +  getDescription(destinationName)
                            + ")[" + getDescription(destinationName));
                    visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
                }
                else {
                // case of objects
                // Get the U.class
//                visitor.visitLdcInsn(Type.getType(descriptionSetter));
                // TODO : copy object, or map it if null !
                // TODO : handle collections
                }
            }
        }
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(3,3);
        visitor.visitEnd();

        writer.visitEnd();

        Class<?> resultClass = loader.defineClass(className.replaceAll("[/]", "."), writer.toByteArray());
        @SuppressWarnings("unchecked")
        Copier<T,U> instance =  (Copier<T,U>) resultClass.getConstructor(CopyContext.class).newInstance(ctx);
        ctx.registerCopier(src, destination, instance);
        ctx.createRequiredCopiers();
        return instance;
	}

    public static <T> Copier<T, T> createCopier(Class<T> clazz, CopyContext ctx)
            throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        return createCopier(clazz, clazz, ctx);

    }

    private static List<Tuple<Property, Property>> listCompatibelProperties(Class<?> source, Class<?> destination) {
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

    private static Property resolveTargetProperty(Property src, Map<String, Property> targetProperties) {
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

    private static String getDescription(String className){
        if(className.indexOf('/') < 0) {
            return className;
        }
        return 'L' + className + ';';
    }
}
