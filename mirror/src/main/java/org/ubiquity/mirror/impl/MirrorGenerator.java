package org.ubiquity.mirror.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
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
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Constants.JAVA_VERSION, Opcodes.ACC_PUBLIC, name,
                "Lorg/ubiquity/mirror/impl/AbstractMirror<L" + byteCodeName(aClass.getName()) + ";>",
                "org/ubiquity/mirror/impl/AbstractMirror", null);
        generateConstructor(writer);
        Map<String, ClassDefinition> definitions = makeClasses(writer, properties, name);
        generateBuildProperties(writer, definitions);
        writer.visitEnd();
        List<ClassDefinition> result = Lists.newArrayList();
        result.addAll(definitions.values());
        result.add(new ClassDefinition(name, writer.toByteArray()));
        return result;
    }

    private static void generateConstructor(ClassWriter writer) {
        MethodVisitor visitor = writer.visitMethod(ClassFileWriter.ACC_PUBLIC, "<init>", null, "()V", null);
        visitor.visitIntInsn(ALOAD, 0);
        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/mirror/impl/AbstractMirror", "<init>", "()V");
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();
    }

    private static Map<String, ClassDefinition> makeClasses(ClassWriter writer, Map<String, BytecodeProperty> properties,
                                                   String mirrorClassName) {
        Map<String, ClassDefinition>  result = Maps.newHashMap();
        for (Map.Entry<String, BytecodeProperty> entry : properties.entrySet()) {
            BytecodeProperty property = entry.getValue();
            String innerClassSimpleName = property.getName() + "$" + SEQUENCE.incrementAndGet();
            String innerClassName = mirrorClassName + "$" + innerClassSimpleName;
            writer.visitInnerClass(innerClassName, mirrorClassName, innerClassSimpleName, ACC_PROTECTED);
            // TODO : implement.me : create the actual inner classes
            result.put(property.getName(), new ClassDefinition(innerClassName, new byte[0]));
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

    private static byte[] createInnerClass(String name, String innerName, BytecodeProperty property) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Constants.JAVA_VERSION, ACC_PUBLIC, name, "", "", null);

        return writer.toByteArray();
    }
}
