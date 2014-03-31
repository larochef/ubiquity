/*
 * Copyright 2012 ubiquity-copy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ubiquity.mirror.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.objectweb.asm.*;
import org.ubiquity.util.ClassDefinition;
import org.ubiquity.util.Constants;
import org.ubiquity.util.visitors.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.objectweb.asm.Opcodes.*;
import static org.ubiquity.util.ByteCodeStringHelper.*;

/**
 * TODO : document.me properly !!
 *
 * Generate mirrors bytecode
 */
public final class MirrorGenerator {

    private static final String MIRROR_PREFIX = "org/ubiquity/mirror/Mirror$";
    private static final AtomicLong SEQUENCE = new AtomicLong();
    private static final String BUILD_PROPERTIES_SIGNATURE =
            "()Ljava/util/Map<Ljava/lang/String;Lorg/ubiquity/mirror/Property<Lorg/ubiquity/mirror/objects/ValueObject;>;>;";

    private MirrorGenerator() {
        // I am a utility class
    }

    public static Collection<ClassDefinition> generateMirror(Class<?> aClass, Map<String, Class<?>> generics)
            throws IOException {
        ClassReader reader = new ClassReader(aClass.getName());
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(new GenericsVisitor(visitor, generics), 0);
        Map<String, BytecodeProperty> properties = visitor.getProperties();
        String name = generateMirrorName(aClass);
        String handledClassName = byteCodeName(aClass.getName());
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Constants.JAVA_VERSION, Opcodes.ACC_PUBLIC, name,
                "Lorg/ubiquity/mirror/impl/AbstractMirror<" + getDescription(handledClassName) + ">;",
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
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
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
            writer.visitInnerClass(innerClassName, mirrorClassName, innerClassSimpleName, ACC_PUBLIC);
            byte[] innerClass = createInnerClass(innerClassName, innerClassSimpleName, mirrorClassName, handledClass, property);
            result.put(property.getName(), new ClassDefinition(innerClassName, innerClass));
        }
        return result;
    }

    private static void generateBuildProperties(ClassWriter writer, Map<String, ClassDefinition> definitions) {
        MethodVisitor visitor = writer.visitMethod(ACC_PROTECTED, "buildProperties",
                "()Ljava/util/Map;", BUILD_PROPERTIES_SIGNATURE, null);
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

        String resolvedType = getDescription(property.getType());
        if(property.isPrimitive()) {
            resolvedType = Constants.SIMPLE_PROPERTIES.get(resolvedType);
        }
        writer.visit(Constants.JAVA_VERSION, ACC_PUBLIC, name,
                "Lorg/ubiquity/mirror/impl/AbstractProperty<"
                        + getDescription(handledClass) + resolvedType + ">;",
                "org/ubiquity/mirror/impl/AbstractProperty", null
        );

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

        if(!property.getAnnotations().isEmpty()) {
            createGetAnnotations(writer, property);
        }
        writer.visitEnd();
        return writer.toByteArray();
    }

    private static void createGetAnnotations(ClassWriter writer, BytecodeProperty property) {
        MethodVisitor visitor = writer.visitMethod(ACC_PROTECTED, "buildAnnotations", "()Ljava/util/List;",
                "()Ljava/util/List<Lorg/ubiquity/mirror/Annotation;>;", null);
        visitor.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableList", "builder",
                "()Lcom/google/common/collect/ImmutableList$Builder;");
        for(Annotation annotation : property.getAnnotations()) {

            mapAnnotation(visitor, annotation);

            visitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableList$Builder",
                    "add", "(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;");
        }
        visitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableList$Builder", "build",
                "()Lcom/google/common/collect/ImmutableList;");
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private static void mapAnnotation(MethodVisitor visitor, Annotation annotation) {
        visitor.visitTypeInsn(NEW, "org/ubiquity/mirror/Annotation");
        visitor.visitInsn(DUP);
        visitor.visitLdcInsn(Type.getType(annotation.getClazz()));
        if(annotation.isVisible()) {
            visitor.visitInsn(ICONST_1);
        }
        else {
            visitor.visitInsn(ICONST_0);
        }
        visitor.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableMap", "builder",
                "()Lcom/google/common/collect/ImmutableMap$Builder;");

        for(Map.Entry<String, AnnotationProperty> prop : annotation.getProperties().entrySet()) {
            visitor.visitLdcInsn(prop.getKey());
            mapAnnotationProperty(visitor, prop.getValue());
            visitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMap$Builder", "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;");
        }
        visitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMap$Builder", "build",
                "()Lcom/google/common/collect/ImmutableMap;");

        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/mirror/Annotation", "<init>",
                "(Ljava/lang/Class;ZLjava/util/Map;)V");
    }

    private static void mapAnnotationProperty(MethodVisitor visitor, AnnotationProperty property) {
        visitor.visitTypeInsn(NEW, "org/ubiquity/mirror/AnnotationProperty");
        visitor.visitInsn(DUP);
        visitor.visitLdcInsn(property.getName());
        visitor.visitLdcInsn(Type.getType(toJavaClass(byteCodeName(property.getDesc()))));
        mapAnnotationValue(visitor, property.getValue());
        visitor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/mirror/AnnotationProperty", "<init>",
                "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)V");
    }

    private static void mapAnnotationValue(MethodVisitor visitor, Object value) {
        Class valueClass = value.getClass();
        visitor.visitLdcInsn(value);
        if(valueClass == Integer.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        } else if(valueClass == Short.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
        } else if(valueClass == Long.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
        } else if(valueClass == Float.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
        } else if(valueClass == Double.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
        } else if(valueClass == Byte.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
        } else if(valueClass == Boolean.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
        } else if(valueClass == Character.class) {
            visitor.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
        } else if(valueClass == String.class) {
            // Nothing seems to be needed
        } else {
            throw new IllegalArgumentException("Unable to map (yet) class of type " + valueClass.getName());
        }
    }

    private static void createInnerClassConstructor(ClassWriter writer, BytecodeProperty property) {
        MethodVisitor constructor = writer.visitMethod(ACC_PROTECTED, "<init>", "()V", null, null);
        constructor.visitIntInsn(ALOAD, 0);
        constructor.visitLdcInsn(property.getName());
        String propertyObjectName = property.getType();
        if(property.isPrimitive()) {
            propertyObjectName = byteCodeName(Constants.SIMPLE_PROPERTIES.get(getDescription(propertyObjectName)));
        }
        constructor.visitLdcInsn(Type.getObjectType(propertyObjectName));
        constructor.visitMethodInsn(INVOKESPECIAL, "org/ubiquity/mirror/impl/AbstractProperty", "<init>",
                "(Ljava/lang/String;Ljava/lang/Class;)V");
        constructor.visitInsn(RETURN);
        constructor.visitMaxs(0, 0);
        constructor.visitEnd();
    }

    private static void createGet(ClassWriter writer, BytecodeProperty property, String handledClassName,
                                  String innerClassName) {
        // Create actual get code
        String resolvedArgumentType = getDescription(property.getTypeGetter());
        if(property.isPrimitive()) {
            resolvedArgumentType = Constants.SIMPLE_PROPERTIES.get(resolvedArgumentType);
        }
        String description = "(" + getDescription(handledClassName) + ")" + resolvedArgumentType;
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "get", description, null, null);

        visitor.visitIntInsn(ALOAD, 1);
        visitor.visitMethodInsn(INVOKEVIRTUAL, handledClassName, property.getGetter(),
                "()" + getDescription(property.getTypeGetter()));
        if(property.isPrimitive()) {
            visitor.visitMethodInsn(INVOKESTATIC, "org/ubiquity/util/NativeConverter", "convert",
                    "(" + property.getType() + ")" + resolvedArgumentType);
        }
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();

        // Create bridge code
        visitor = writer.visitMethod(ACC_PUBLIC | ACC_BRIDGE | ACC_VOLATILE | ACC_SYNTHETIC, "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        visitor.visitIntInsn(ALOAD, 0);
        visitor.visitIntInsn(ALOAD, 1);
        visitor.visitTypeInsn(CHECKCAST, byteCodeName(handledClassName));
        visitor.visitMethodInsn(INVOKEVIRTUAL, innerClassName, "get", description);
        visitor.visitInsn(ARETURN);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private static void createSet(ClassWriter writer, BytecodeProperty property, String handledClassName,
                                  String innerClassName) {
        // Create actual code
        String resolvedArgumentType = getDescription(property.getTypeSetter());
        if(property.isPrimitive()) {
            resolvedArgumentType = Constants.SIMPLE_PROPERTIES.get(resolvedArgumentType);
        }
        String description = "(" + getDescription(handledClassName) + resolvedArgumentType + ")V";
        MethodVisitor visitor = writer.visitMethod(ACC_PUBLIC, "set", description, null, null);
        visitor.visitIntInsn(ALOAD, 1);
        visitor.visitIntInsn(ALOAD, 2);
        if(property.isPrimitive()) {
            visitor.visitMethodInsn(INVOKESTATIC, "org/ubiquity/util/NativeConverter", "convert",
                    "(" + resolvedArgumentType + ")" + property.getType());
        }
        visitor.visitMethodInsn(INVOKEVIRTUAL, handledClassName, property.getSetter(),
                "(" + getDescription(property.getTypeSetter()) + ")V");
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
        visitor.visitTypeInsn(CHECKCAST, byteCodeName(resolvedArgumentType));
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
