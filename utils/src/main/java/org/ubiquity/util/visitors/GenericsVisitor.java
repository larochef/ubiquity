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
package org.ubiquity.util.visitors;

import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

import static org.ubiquity.util.BytecodeStringUtils.*;
import static org.ubiquity.util.Constants.ASM_LEVEL;

/**
 * Visitor used to make generics transparent, as if the class wasn't "generified".
 */
public final class GenericsVisitor extends ClassVisitor {

    private final Map<String, Class<?>> generics;

    public GenericsVisitor(ClassVisitor classVisitor, Map<String, Class<?>> generics) {
        super(ASM_LEVEL, classVisitor);
        this.generics = generics == null ? ImmutableMap.<String, Class<?>>of() : ImmutableMap.copyOf(generics);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // if generics are matched, replace the generic by the concrete type defined.
        String newSignature = signature;
        String newDesc = desc;
        if(signature != null) {
            for (Map.Entry<String, Class<?>> entry : generics.entrySet()) {
                newSignature = replaceAll(newSignature,
                        "T" + entry.getKey() + ";",
                        getDescription(byteCodeName(entry.getValue().getName())));
            }
            newDesc = signatureToDesc(newSignature);
        }

        return super.visitMethod(access, name, newDesc, newSignature, exceptions);
    }

}
