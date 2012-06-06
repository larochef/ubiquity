package org.ubiquity.bytecode;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.ubiquity.util.Tuple;

import java.util.Map;

import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_VOLATILE;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.ubiquity.util.Constants.COLLECTIONS;
import static org.ubiquity.util.Constants.SIMPLE_PROPERTIES;

/**
 * Date: 21/04/12
 *
 * @author François LAROCHE
 */
final class GeneratorHelper {

    private GeneratorHelper() {}

    static void createConstructor(ClassWriter writer, String className, Map<String, Tuple<Property, Property>> requiredCopiers) {
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/ubiquity/bytecode/CopyContext;)V", null, null);
        Label label1 = new Label();
        visitor.visitLabel(label1);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/bytecode/SimpleCopier", "<init>", "(Lorg/ubiquity/bytecode/CopyContext;)V");

        for(String key : requiredCopiers.keySet()) {
            Tuple<Property, Property> tuple = requiredCopiers.get(key);
            visitor.visitVarInsn(ALOAD, 0);
            if(COLLECTIONS.contains(getDescription(tuple.tObject.getTypeGetter()))) {
                visitor.visitLdcInsn(Type.getType(getDescription(tuple.tObject.getDefaultGenericsGetterValue())));
                visitor.visitLdcInsn(Type.getType(getDescription(tuple.uObject.getDefaultGenericsSetterValue())));
            }
            else {
                visitor.visitLdcInsn(Type.getType(getDescription(tuple.tObject.getTypeGetter())));
                visitor.visitLdcInsn(Type.getType(getDescription(tuple.uObject.getTypeSetter())));
            }
            visitor.visitMethodInsn(INVOKESTATIC, "org/ubiquity/util/CopierKey", "newBuilder", "(Ljava/lang/Class;Ljava/lang/Class;)Lorg/ubiquity/util/CopierKey$Builder;");
            if(tuple.tObject.getGenericGetter() != null && !COLLECTIONS.contains(getDescription(tuple.tObject.getTypeGetter()))) {
                for(String genKey : tuple.tObject.getGenericGetter().keySet()) {
                    visitor.visitLdcInsn(genKey);
                    visitor.visitLdcInsn(tuple.tObject.getGenericGetter().get(genKey));
                    visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/util/CopierKey$Builder", "sourceAnnotation",
                            "(Ljava/lang/String;Ljava/lang/String;)Lorg/ubiquity/util/CopierKey$Builder;");
                }
            }
            if(tuple.uObject.getGenericSetter() != null && !COLLECTIONS.contains(getDescription(tuple.tObject.getTypeGetter()))) {
                for(String genKey : tuple.uObject.getGenericSetter().keySet()) {
                    visitor.visitLdcInsn(genKey);
                    visitor.visitLdcInsn(tuple.uObject.getGenericSetter().get(genKey));
                    visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/util/CopierKey$Builder", "destinationAnnotation",
                            "(Ljava/lang/String;Ljava/lang/String;)Lorg/ubiquity/util/CopierKey$Builder;");
                }
            }
            visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/util/CopierKey$Builder", "build", "()Lorg/ubiquity/util/CopierKey;");
            visitor.visitFieldInsn(PUTFIELD, className, key, "Lorg/ubiquity/util/CopierKey;");
        }

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

    static void createCopierKeys(ClassWriter writer, Map<String, Tuple<Property, Property>> requiredCopiers) {
        for(String key : requiredCopiers.keySet()) {
            writer.visitField(ACC_PRIVATE, key, "Lorg/ubiquity/util/CopierKey;", null, null).visitEnd();
        }
    }

    static void handleComplexObjects(MethodVisitor visitor, String className, String srcName, String destinationName, Tuple<Property, Property> p, Map<String, Tuple<Property, Property>> requiredCopiers) {
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
        visitor.visitFieldInsn(GETFIELD, "org/ubiquity/bytecode/SimpleCopier", "context", "Lorg/ubiquity/bytecode/CopyContext;");
        String copierName = "COPIER_" + requiredCopiers.size();
        requiredCopiers.put(copierName, p);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitFieldInsn(GETFIELD, className, copierName, "Lorg/ubiquity/util/CopierKey;");
        visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/bytecode/CopyContext", "getCopier", "(Lorg/ubiquity/util/CopierKey;)Lorg/ubiquity/Copier;");
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

    static void handeArrays(MethodVisitor visitor, String className, String srcName, String destinationName, Tuple<Property, Property> p, Map<String, Tuple<Property, Property>> requiredCopiers) {
        String descriptionGetter = getDescription(p.tObject.getTypeGetter());
        String descriptionSetter = getDescription(p.uObject.getTypeSetter());
        String typeDescriptionGetter = descriptionGetter.substring(1);
        // arrays needing unchecked conversion
        if(SIMPLE_PROPERTIES.containsKey(typeDescriptionGetter)) {
            if(!descriptionGetter.equals(descriptionSetter)) {
                // handle simple arrays
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitVarInsn(ALOAD, 0);
                visitor.visitVarInsn(ALOAD, 1);
                visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + getDescription(p.tObject.getTypeGetter()));
                visitor.visitMethodInsn(INVOKEVIRTUAL, className, "convert",
                        "(" + getDescription(p.tObject.getTypeGetter()) + ")" + getDescription(p.uObject.getTypeSetter()));
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + getDescription(p.uObject.getTypeSetter()) + ")V");
                return;
            }
            // Simple arrays cloned from a to b
            visitor.visitVarInsn(ALOAD, 1);
            visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + getDescription(p.tObject.getTypeGetter()));
            Label arrayNotNull = new Label();
            visitor.visitJumpInsn(IFNONNULL, arrayNotNull);
            visitor.visitVarInsn(ALOAD, 2);
            visitor.visitInsn(ACONST_NULL);
            visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + getDescription(p.uObject.getTypeSetter()) + ")V");
            Label arrayNullEnd = new Label();
            visitor.visitJumpInsn(GOTO, arrayNullEnd);
            visitor.visitLabel(arrayNotNull);
            visitor.visitVarInsn(ALOAD, 2);
            visitor.visitVarInsn(ALOAD, 1);
            visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + getDescription(p.tObject.getTypeGetter()));
            visitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "clone", "()Ljava/lang/Object;");
            visitor.visitTypeInsn(CHECKCAST, getDescription(p.uObject.getTypeSetter()));
            visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
            visitor.visitLabel(arrayNullEnd);
            return;
        }
        // Arrays of complex types
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitFieldInsn(GETFIELD, "org/ubiquity/bytecode/SimpleCopier", "context", "Lorg/ubiquity/bytecode/CopyContext;");
        String copierName = "COPIER_" + requiredCopiers.size();
        requiredCopiers.put(copierName, p);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitFieldInsn(GETFIELD, className, copierName, "Lorg/ubiquity/util/CopierKey;");
        visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/bytecode/CopyContext", "getCopier", "(Lorg/ubiquity/util/CopierKey;)Lorg/ubiquity/Copier;");
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + descriptionGetter);
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()" + getDescription(p.uObject.getTypeGetter()));
        visitor.visitMethodInsn(INVOKEINTERFACE, "org/ubiquity/Copier", "map", "([Ljava/lang/Object;[Ljava/lang/Object;)[Ljava/lang/Object;");
        visitor.visitTypeInsn(CHECKCAST, getDescription(p.uObject.getTypeSetter()));
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + descriptionSetter + ")V");
    }

    static void handleCollection(MethodVisitor visitor, String className, Tuple<Property, Property> p, String collectionType, String srcName, String destinationName, Map<String, Tuple<Property, Property>> requiredCopiers) {
        String tGeneric = p.tObject.getDefaultGenericsGetterValue();
        String uGeneric = p.uObject.getDefaultGenericsSetterValue();
        if(SIMPLE_PROPERTIES.containsKey(tGeneric)) {
            if(tGeneric.equals(uGeneric)) {
                Label l0 = new Label();
                visitor.visitVarInsn(ALOAD, 1);
                visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + getDescription(p.tObject.getTypeGetter()));
                visitor.visitJumpInsn(IFNONNULL, l0);
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitInsn(ACONST_NULL);
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + getDescription(p.uObject.getTypeSetter()) + ")V");
                Label l2 = new Label();
                visitor.visitJumpInsn(GOTO, l2);
                visitor.visitLabel(l0);
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()" + getDescription(p.uObject.getTypeGetter()));
                Label l1 = new Label();
                visitor.visitJumpInsn(IFNONNULL, l1);
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitVarInsn(ALOAD, 0);
                visitor.visitFieldInsn(GETFIELD, "org/ubiquity/bytecode/SimpleCopier", "context", "Lorg/ubiquity/bytecode/CopyContext;");
                visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/bytecode/CopyContext", "getFactory", "()Lorg/ubiquity/CollectionFactory;");
                visitor.visitMethodInsn(INVOKEINTERFACE, "org/ubiquity/CollectionFactory", "new" + collectionType, "()Ljava/util/" + collectionType + ";");
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(" + getDescription(p.uObject.getTypeSetter()) + ")V");
                visitor.visitLabel(l1);
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()" + getDescription(p.uObject.getTypeGetter()));
                visitor.visitMethodInsn(INVOKEINTERFACE, "java/util/" + collectionType, "clear", "()V");
                visitor.visitVarInsn(ALOAD, 2);
                visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()" + getDescription(p.uObject.getTypeGetter()));
                visitor.visitVarInsn(ALOAD, 1);
                visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()" + getDescription(p.tObject.getTypeGetter()));
                if("Map".equals(collectionType)) {
                    visitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "putAll", "(Ljava/util/Map;)V");
                }
                else {
                    visitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Collection", "addAll", "(Ljava/util/Collection;)Z");
                    visitor.visitInsn(POP);
                }
                visitor.visitLabel(l2);
            }
            // List from T is of a simple type but not of U !!
            return;
        }
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, srcName, p.tObject.getGetter(), "()Ljava/util/" + collectionType + ";");
        visitor.visitVarInsn(ALOAD, 2);
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getGetter(), "()Ljava/util/" + collectionType + ";");
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitFieldInsn(GETFIELD, "org/ubiquity/bytecode/SimpleCopier", "context", "Lorg/ubiquity/bytecode/CopyContext;");
        visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/bytecode/CopyContext", "getFactory", "()Lorg/ubiquity/CollectionFactory;");
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitFieldInsn(GETFIELD, "org/ubiquity/bytecode/SimpleCopier", "context", "Lorg/ubiquity/bytecode/CopyContext;");
        String copierName = "COPIER_" + requiredCopiers.size();
        requiredCopiers.put(copierName, p);
        visitor.visitVarInsn(ALOAD, 0);
        visitor.visitFieldInsn(GETFIELD, className, copierName, "Lorg/ubiquity/util/CopierKey;");
        visitor.visitMethodInsn(INVOKEVIRTUAL, "org/ubiquity/bytecode/CopyContext", "getCopier", "(Lorg/ubiquity/util/CopierKey;)Lorg/ubiquity/Copier;");
        visitor.visitMethodInsn(INVOKESTATIC, "org/ubiquity/bytecode/SimpleCopier", "handle" + collectionType,
                "(Ljava/util/" + collectionType + ";Ljava/util/" + collectionType + ";Lorg/ubiquity/CollectionFactory;Lorg/ubiquity/Copier;)Ljava/util/" + collectionType + ";");
        visitor.visitMethodInsn(INVOKEVIRTUAL, destinationName, p.uObject.getSetter(), "(Ljava/util/" + collectionType + ";)V");
    }

    static String getDescription(String className){
        if(className.indexOf('/') < 0) {
            return className;
        }
        if(className.startsWith("[")) {
            return "[" + getDescription(className.substring(1));
        }
        if(className.startsWith("L") && className.endsWith(";")) {
            return className;
        }
        return 'L' + className + ';';
    }

    static String createCopierClassName(String srcBytecodeName, String targetBytecodeName) {
        int index = srcBytecodeName.startsWith("L") ? 1 : 0;
        String rightPart = srcBytecodeName.substring(index);
        index = targetBytecodeName.startsWith("L") ? 1 : 0;
        rightPart += targetBytecodeName.substring(index);
        return "org/ubiquity/bytecode/generated/Copier" + rightPart.replaceAll("[/]", "").replaceAll(";", "") + System.nanoTime();
    }

}
