package org.ubiquity.bytecode;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.ubiquity.util.Tuple;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * Date: 21/04/12
 *
 * @author François LAROCHE
 */
final class GeneratorHelper {

    private GeneratorHelper() {}

    static void createConstructor(ClassWriter writer, String className) {
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

    static void createNewInstance(ClassWriter writer, String className, String destinationName) {
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

    static void createNewArray(ClassWriter writer, String className, String destinationName) {
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

    static void createCopyBridge(ClassWriter writer, String className, String srcName, String destinationName) {
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

    static void handleComplexObjects(MethodVisitor visitor, String className, String srcName, String destinationName, Tuple<Property, Property> p) {
        String descriptionGetter = getDescription(p.tObject.getTypeGetter());
        String descriptionSetter = getDescription(p.uObject.getTypeSetter());
        Label notNullLabel = new Label();
        Label nullLabel = new Label();
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
        visitor.visitJumpInsn(IFNONNULL, notNullLabel);
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitInsn(ACONST_NULL);
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
        visitor.visitJumpInsn(GOTO, nullLabel);
        visitor.visitLabel(notNullLabel);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitFieldInsn(GETFIELD, className, "context", "Lorg/ubiquity/bytecode/CopyContext;");
        visitor.visitLdcInsn(Type.getType(descriptionGetter));
        visitor.visitLdcInsn(Type.getType(descriptionSetter));
        visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/bytecode/CopyContext", "getCopier", "(Ljava/lang/Class;Ljava/lang/Class;)Lorg/ubiquity/Copier;");
        visitor.visitVarInsn(ASTORE, 3);
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()" + getDescription(p.uObject.getTypeGetter()));
        Label notNull2 = new Label();
        visitor.visitJumpInsn(IFNONNULL, notNull2);
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitVarInsn(ALOAD, 3);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
        visitor.visitMethodInsn(INVOKEINTERFACE, "org/ubiquity/Copier", "map", "(Ljava/lang/Object;)Ljava/lang/Object;");
        visitor.visitTypeInsn(CHECKCAST, p.uObject.getTypeSetter());
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
        visitor.visitJumpInsn(GOTO, nullLabel);
        visitor.visitLabel(notNull2);
        visitor.visitVarInsn(ALOAD, 3);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()" + getDescription(p.uObject.getTypeGetter()));
        visitor.visitMethodInsn(INVOKEINTERFACE, "org/ubiquity/Copier", "copy", "(Ljava/lang/Object;Ljava/lang/Object;)V");
        visitor.visitLabel(nullLabel);
    }

    static String getDescription(String className){
        if(className.indexOf('/') < 0) {
            return className;
        }
        return 'L' + className + ';';
    }
}
