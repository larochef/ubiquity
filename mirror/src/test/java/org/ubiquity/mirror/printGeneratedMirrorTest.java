package org.ubiquity.mirror;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public class PrintGeneratedMirrorTest {

    @Test
    public void printGeneratedMirror() throws Exception {
        ClassReader reader = new ClassReader(GeneratedMirror.class.getName() + "$Property1");
        ClassVisitor visitor = new TraceClassVisitor(new PrintWriter(System.out));
        reader.accept(visitor, 0);
    }

}
