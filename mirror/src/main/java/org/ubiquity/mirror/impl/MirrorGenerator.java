package org.ubiquity.mirror.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.objectweb.asm.*;
import org.ubiquity.util.ClassDefinition;
import org.ubiquity.util.Constants;
import org.ubiquity.util.visitors.BytecodeProperty;
import org.ubiquity.util.visitors.PropertyRetrieverVisitor;
import sun.org.mozilla.classfile.internal.ClassFileWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.objectweb.asm.Opcodes.*;
import static org.ubiquity.util.BytecodeStringUtils.byteCodeName;
import static org.ubiquity.util.BytecodeStringUtils.getDescription;

/**
 *
 */
public final class MirrorGenerator {

    private static final String MIRROR_PREFIX = "org/ubiquity/mirror/Mirror$";
    private static final AtomicLong SEQUENCE = new AtomicLong();
    private static final String BUILD_PROPERTIES_SIGNATURE =
            "()Ljava/util/Map<Ljava/lang/String;Lorg/ubiquity/mirror/Property<Lorg/ubiquity/mirror/objects/ValueObject;*>;>;";

    private MirrorGenerator() {
        // I am a utility class
    }

    public static Collection<ClassDefinition> generateMirror(Class<?> aClass) throws IOException {
        ClassReader reader = new ClassReader(aClass.getName());
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
        Map<String, BytecodeProperty> properties = visitor.getProperties();
        String name = generateMirrorName(aClass);
        String handledClassName = byteCodeName(aClass.getName());
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Constants.JAVA_VERSION, Opcodes.ACC_PUBLIC, name,
                "Lorg/ubiquity/mirror/impl/AbstractMirror<L" + handledClassName + ";>",
                "org/ubiquity/mirror/impl/AbstractMirror", null);
        generateConstructor(writer);
        Map<String, ClassDefinition> definitions = makeClasses(writer, properties, name, handledClassName);
        generateBuildProperties(writer, definitions);
        writer.visitEnd();
        List<ClassDefinition> result = Lists.newArrayList();
        result.addAll(definitions.values());
        result.add(new ClassDefinition(name, writer.toByteArray()));
        return result;
    }

    private static void generateConstructor(ClassWriter writer) {
        MethodVisitor visitor = writer.visitMethod(ClassFileWriter.ACC_PUBLIC, "<init>", "()V", null, null);
        visitor.visitIntInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/mirror/impl/AbstractMirror", "<init>", "()V");
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();
    }

    private static Map<String, ClassDefinition> makeClasses(ClassWriter writer, Map<String, BytecodeProperty> properties,
                                                   String mirrorClassName, String handledClass) {
        Map<String, ClassDefinition>  result = Maps.newHashMap();
        for (Map.Entry<String, BytecodeProperty> entry : properties.entrySet()) {
            BytecodeProperty property = entry.getValue();
            String innerClassSimpleName = property.getName();
            String innerClassName = mirrorClassName + "$" + innerClassSimpleName;
            writer.visitInnerClass(innerClassName, mirrorClassName, innerClassSimpleName, ACC_PROTECTED);
            byte[] innerClass = createInnerClass(innerClassName, innerClassSimpleName, mirrorClassName, handledClass, property);
            result.put(property.getName(), new ClassDefinition(innerClassName, innerClass));
        }
        return result;
    }

    private static void generateBuildProperties(ClassWriter writer, Map<String, ClassDefinition> definitions) {
        MethodVisitor visitor = writer.visitMethod(ACC_PROTECTED, "buildProperties",
                "()Ljava/util/Map", BUILD_PROPERTIES_SIGNATURE, null);
        visitor.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableMap",
                "builder", "()Lcom/google/common/collect/ImmutableMap$Builder;");
        for(Map.Entry<String, ClassDefinition> entry : definitions.entrySet()) {
            visitor.visitLdcInsn(entry.getKey());
            String byteCodeName = entry.getValue().getClassName();
            visitor.visitTypeInsn(NEW, byteCodeName);
            visitor.visitInsn(DUP);
            visitor.visitMethodInsn(INVOKESPECIAL, byteCodeName, "<init>", "()V");
            visitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMap$Builder", "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;");
        }
        visitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMap$Builder", "build",
                "()Lcom/google/common/collect/ImmutableMap;");
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private static String generateMirrorName(Class<?> c) {
        return MIRROR_PREFIX + c.getSimpleName() + "$" + SEQUENCE.incrementAndGet();
    }

    private static byte[] createInnerClass(String name, String innerName, String mirrorClass, String handledClass,
                                           BytecodeProperty property) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Constants.JAVA_VERSION, ACC_PUBLIC, name,
                "Lorg/ubiquity/mirror/impl/AbstractProperty<"
                        + getDescription(handledClass) + getDescription(property.getTypeGetter()) + ">;",
                "org/ubiquity/mirror/impl/AbstractProperty", null);

        writer.visitInnerClass(name, mirrorClass, innerName, ACC_STATIC | ACC_PUBLIC);

        createInnerClassConstructor(writer, property);
        if(property.isReadable()) {
            createGet(writer, property, handledClass, name);
            createBooleanMethod(writer, "isReadable");
        }
        if(property.isWritable()) {
            createSet(writer, property, handledClass, name);
            createBooleanMethod(writer, "isWritable");
        }

        return writer.toByteArray();
    }

    private static void createInnerClassConstructor(ClassWriter writer, BytecodeProperty property) {
        MethodVisitor constructor = writer.visitMethod(ACC_PROTECTED, "<init>", "()V", null, null);
        constructor.visitIntInsn(ALOAD, 0);
        constructor.visitLdcInsn(property.getName());
        constructor.visitLdcInsn(Type.getObjectType(getDescription(property.getTypeGetter())));
        constructor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/mirror/impl/AbstractProperty", "<init>",
                "(Ljava/lang/String;Ljava/lang/Class;)V");
        constructor.visitMaxs(0, 0);
        constructor.visitEnd();
    }

    private static void createGet(ClassWriter writer, BytecodeProperty property, String handledClassName,
                                  String innerClassName) {
        // Create actual get code
        String description = "(" + getDescription(handledClassName) + ")" + getDescription(property.getTypeGetter());
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "get", description, null, null);

        visitor.visitIntInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, handledClassName, property.getGetter(), "()"
                + getDescription(property.getTypeGetter()));
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();

        // Create bridge code
        visitor = writer.visitMethod(ACC_PUBLIC | ACC_BRIDGE | ACC_VOLATILE | ACC_SYNTHETIC, "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        visitor.visitIntInsn(ALOAD, 0);
        visitor.visitIntInsn(ALOAD, 1);
        visitor.visitTypeInsn(CHECKCAST, handledClassName);
        visitor.visitMethodInsn(INVOKEVIRTUAL, innerClassName, "get", description);
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private static void createSet(ClassWriter writer, BytecodeProperty property, String handledClassName,
                                  String innerClassName) {
        // Create actual code
        String description = "(" + getDescription(handledClassName) + getDescription(property.getTypeSetter()) + ")V";
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "set", description, null, null);
        visitor.visitIntInsn(ALOAD, 1);
        visitor.visitIntInsn(ALOAD, 2);
        visitor.visitMethodInsn(INVOKEVIRTUAL, handledClassName, property.getSetter(), "(" + handledClassName + ")V");
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();

        // create bridge
        visitor = writer.visitMethod(ACC_PUBLIC | ACC_BRIDGE | ACC_VOLATILE | ACC_SYNTHETIC, "set",
                "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
        visitor.visitIntInsn(ALOAD, 0);
        visitor.visitIntInsn(ALOAD, 1);
        visitor.visitTypeInsn(CHECKCAST, handledClassName);
        visitor.visitIntInsn(ALOAD, 2);
        visitor.visitTypeInsn(CHECKCAST, byteCodeName(property.getTypeSetter()));
        visitor.visitMethodInsn(INVOKEVIRTUAL, innerClassName, "set", description);
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private static void createBooleanMethod(ClassWriter writer, String name) {
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, name, "()Z", null, null);
        visitor.visitInsn(ICONST_1);
        visitor.visitInsn(IRETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }
}
