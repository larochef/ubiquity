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

import com.google.common.collect.Lists;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.ubiquity.util.Constants;

import java.util.Collection;

/**
 * Parser used to parse properties
 */
final class MethodParser extends MethodVisitor {

    private Collection<AnnotationParser> parsers;
    private final BytecodeProperty bytecodeProperty;

    public MethodParser(BytecodeProperty bytecodeProperty) {
        super(Constants.ASM_LEVEL);
        this.parsers = Lists.newArrayList();
        this.bytecodeProperty = bytecodeProperty;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationParser parser = new AnnotationParser(desc, visible);
        this.parsers.add(parser);
        return parser;
    }

    @Override
    public void visitEnd() {
        for (AnnotationParser parser : parsers) {
            bytecodeProperty.getAnnotations().add(parser.getAnnotation());
        }
    }
}
