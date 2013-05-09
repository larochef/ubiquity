package org.ubiquity.mirror;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

/**
 *
 */
public class PrintMirrorTest {

    @Test
    public void test() throws Exception {
        ClassReader reader = new ClassReader(GeneratedMirror.class.getName() + "$Property1");
        reader.accept(new TraceClassVisitor(new PrintWriter(System.out)), 0);
    }
}
