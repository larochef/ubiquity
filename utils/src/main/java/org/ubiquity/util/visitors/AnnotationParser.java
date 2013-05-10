/**
 * Copyright 2012 ubiquity-copy

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.ubiquity.util.visitors;

import com.google.common.collect.Maps;
import org.objectweb.asm.AnnotationVisitor;
import org.ubiquity.util.BytecodeStringUtils;
import org.ubiquity.util.Constants;

import java.util.Map;

/**
 *
 */
final class AnnotationParser extends AnnotationVisitor {

    private Annotation annotation;
    private Map<String, AnnotationParser> annotationParsers;
    private Map<String, AnnotationArrayParser> annotationArrayParsers;

    public AnnotationParser (String desc, boolean visible) {
        super(Constants.ASM_LEVEL);
        this.annotationParsers = Maps.newHashMap();
        this.annotationArrayParsers = Maps.newHashMap();
        this.annotation = new Annotation();
        this.annotation.setClazz(desc);
        this.annotation.setVisible(visible);
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public void visit(String name, Object value) {
        AnnotationProperty<Object> property = new AnnotationProperty<Object>();
        property.setName(name);
        property.setValue(value);
        property.setDesc(BytecodeStringUtils.byteCodeName(value.getClass().getName()));
        this.annotation.getProperties().put(name, property);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        AnnotationProperty<String> property = new AnnotationProperty<String>();
        property.setName(name);
        property.setValue(value);
        property.setDesc(desc);
        this.annotation.getProperties().put(name, property);
    }

    @Override
    public void visitEnd() {
        for(Map.Entry<String, AnnotationParser> parser : this.annotationParsers.entrySet()) {
            AnnotationProperty<Annotation> property = new AnnotationProperty<Annotation>();
            property.setName(parser.getKey());
            Annotation annotation = parser.getValue().getAnnotation();
            property.setValue(annotation);
            property.setDesc(annotation.getClazz());
            this.annotation.getProperties().put(parser.getKey(), property);
        }

        for(Map.Entry<String, AnnotationArrayParser> parser : this.annotationArrayParsers.entrySet()) {
            AnnotationProperty<Object[]> property = new AnnotationProperty<Object[]>();
            property.setName(parser.getKey());
            AnnotationArrayParser p = parser.getValue();
            property.setDesc(p.getDesc());
            property.setValue(p.getValues());
            this.annotation.getProperties().put(parser.getKey(), property);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        AnnotationParser parser = new AnnotationParser(desc, true);
        this.annotationParsers.put(name, parser);
        return parser;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        AnnotationArrayParser arrayParser = new AnnotationArrayParser();
        this.annotationArrayParsers.put(name, arrayParser);
        return arrayParser;
    }
}
