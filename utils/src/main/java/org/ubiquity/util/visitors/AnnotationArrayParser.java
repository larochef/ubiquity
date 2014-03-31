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
import org.ubiquity.util.ByteCodeStringHelper;
import org.ubiquity.util.Constants;

import java.util.List;

/**
 * Visitor used to parse array attributes of annotations
 */
final class AnnotationArrayParser extends AnnotationVisitor {

    private List<Object> values;
    private String desc;
    private List<AnnotationParser> annotationParsers;
    private List<AnnotationArrayParser> annotationArrayParsers;

    public AnnotationArrayParser() {
        super(Constants.ASM_LEVEL);
        values = Lists.newArrayList();
        annotationParsers = Lists.newArrayList();
        annotationArrayParsers = Lists.newArrayList();
    }

    @Override
    public void visit(String name, Object value) {
        if(value.getClass().isArray()) {
            this.values.addAll(Lists.newArrayList((Object[]) value));
            this.desc = ByteCodeStringHelper.byteCodeName(value.getClass().getName());
        }
        else {
            values.add(value);
            this.desc = ByteCodeStringHelper.byteCodeName("[" + value.getClass().getName());
        }
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        this.desc = "[" + desc;
        values.add(value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        AnnotationParser parser = new AnnotationParser(desc, true);
        this.annotationParsers.add(parser);
        this.desc = "[" + desc;
        return parser;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        AnnotationArrayParser parser = new AnnotationArrayParser();
        this.annotationArrayParsers.add(parser);
        return parser;
    }

    @Override
    public void visitEnd() {
        for(AnnotationParser parser : annotationParsers) {
            this.values.add(parser.getAnnotation());
        }
        for(AnnotationArrayParser parser : annotationArrayParsers) {
            this.values.addAll(Lists.newArrayList(parser.getValues()));
        }
    }

    public String getDesc() {
        return desc;
    }

    public Object[] getValues() {
        return this.values.toArray(new Object[this.values.size()]);
    }
}
