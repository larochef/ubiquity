package org.ubiquity.mirror.impl;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.ubiquity.util.ClassDefinition;
import org.ubiquity.util.Constants;
import org.ubiquity.util.visitors.BytecodeProperty;
import org.ubiquity.util.visitors.PropertyRetrieverVisitor;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public final class MirrorGenerator {
    private MirrorGenerator() {
        // I am a utility class
    }

    public static ClassDefinition generateMirror(Class<?> aClass) throws IOException {
        ClassReader reader = new ClassReader(aClass.getName());
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
        Map<String, BytecodeProperty> properties = visitor.getProperties();

        String name = generateMirrorName(aClass);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Constants.JAVA_VERSION, Opcodes.ACC_PUBLIC, name, "",
                "org/ubiquity/mirror/impl/AbstractMirror", new String [] {});
        writer.visitEnd();

        return new ClassDefinition(name, writer.toByteArray());
    }

    private static String generateMirrorName(Class<?> c) {
        return "";
    }
}
