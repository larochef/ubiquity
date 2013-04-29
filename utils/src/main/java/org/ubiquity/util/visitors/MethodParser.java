package org.ubiquity.util.visitors;

import com.google.common.collect.Lists;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.ubiquity.util.Constants;

import java.util.Collection;

/**
 *
 */
public class MethodParser extends MethodVisitor {

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
