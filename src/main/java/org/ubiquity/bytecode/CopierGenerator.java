/**
 * 
 */
package org.ubiquity.bytecode;

import java.io.IOException;
import java.util.Map;

import org.objectweb.asm.*;
import org.ubiquity.Copier;
import static  org.objectweb.asm.Opcodes.*;

/**
 * @author François LAROCHE
 *
 */
public class CopierGenerator {
	
	private CopierGenerator() {}

    private static MyClassLoader loader = new MyClassLoader();
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
	
	public static <T, U> Copier<T, U> createCopier(Class<T> src, Class<U> destination) throws IllegalAccessException, InstantiationException {
        Map<String, Property> properties = findProperties(src);
        // TODO : find all compatible properties
        String srcName = byteCodeName(src);
        String destinationName = byteCodeName(destination);
        String className = "org/ubiquity/bytecode/generated/Copier" + src.getSimpleName() + destination.getSimpleName();

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(V1_5, ACC_PUBLIC + ACC_FINAL, className,
                "Lorg/ubiquity/util/SimpleCopier<" + getDescription(srcName) + getDescription(destinationName) + ">;",
                "org/ubiquity/util/SimpleCopier", null);

        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        Label label1 = new Label();
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/util/SimpleCopier", "<init>", "()V");
        visitor.visitInsn(RETURN);
        Label label2 = new Label();
        visitor.visitLabel(label2);
        visitor.visitLocalVariable("this", getDescription(className), null, label1, label2, 0);
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();

        visitor = writer.visitMethod(ACC_PUBLIC + ACC_FINAL, "map", '(' + getDescription(srcName) + ')' + getDescription(destinationName), null, null);
        // Construct result
        visitor.visitTypeInsn(NEW, destinationName);
        visitor.visitInsn(DUP);
        visitor.visitMethodInsn(INVOKESPECIAL, destinationName, "<init>", "()V");
        // Load it in a frame
        visitor.visitVarInsn(ASTORE, 2);
        // End of init

        for(Property p : properties.values()) {
            if(p.isReadable() && p.isWritable()) {
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitVarInsn(ALOAD, 1);
                // TODO : use actual property type !
                visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.getGetter(), "()" + getDescription(p.getTypeGetter()));
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.getSetter(), "(" + getDescription(p.getTypeSetter()) + ")V");
            }
        }
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(0,0);
        visitor.visitEnd();

        visitor = writer.visitMethod(ACC_PUBLIC + ACC_VOLATILE + ACC_BRIDGE, "map", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitTypeInsn(CHECKCAST, srcName);
        visitor.visitMethodInsn(INVOKEVIRTUAL, className, "map", '(' + getDescription(srcName) + ')' + getDescription(destinationName));
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(2, 2);
        writer.visitEnd();

        Class<?> resultClass = loader.defineClass(className.replaceAll("[/]", "."), writer.toByteArray());

        return (Copier<T,U>) resultClass.newInstance();
	}

    public static <T> Copier<T, T> createCopier(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return createCopier(clazz, clazz);

    }
	
	private static String byteCodeName(Class<?> c) {
		return c.getName().replaceAll("[\\.]", "/");
	}

    private static class MyClassLoader extends ClassLoader {
        public Class defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    private static String getDescription(String className){
        if(className.indexOf('/') > 0) {
            return className;
        }
        return 'L' + className + ';';
    }
}
