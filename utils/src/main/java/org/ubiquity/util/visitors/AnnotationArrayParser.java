package org.ubiquity.util.visitors;

import com.google.common.collect.Lists;
import org.objectweb.asm.AnnotationVisitor;
import org.ubiquity.util.Constants;

import java.util.List;

/**
 *
 */
public class AnnotationArrayParser extends AnnotationVisitor {

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
        values.add(value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        this.desc = desc;
        values.add(value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        AnnotationParser parser = new AnnotationParser(desc, true);
        this.annotationParsers.add(parser);
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
            this.values.add(parser.getValues());
        }
    }

    public String getDesc() {
        return desc;
    }

    public Object[] getValues() {
        return this.values.toArray(new Object[this.values.size()]);
    }
}
