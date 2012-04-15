package org.ubiquity.bytecode;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.TraceClassVisitor;
import org.ubiquity.util.Constants;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: famille
 * Date: 14/04/12
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class PrintClassTest {
    @Test
    public void printCodeTest() throws IOException {
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/CodeTest");
        reader.accept(new MyVisitor(new TraceClassVisitor(new PrintWriter(System.out))),0);
    }

    private static class MyVisitor extends ClassVisitor {
        private ClassVisitor v;

        private MyVisitor(ClassVisitor v) {
            super(Constants.ASM_LEVEL, v);
            this.v = v;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            System.out.println("Visiting class :");
            System.out.println("Name = " + name);
            System.out.println("Signature = " + signature);
            System.out.println("superName = " + superName);
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            System.out.println("Visiting method :");
            System.out.println("Name = " + name);
            System.out.println("desc = " + desc);
            System.out.println("signature = " + signature);
            return super.visitMethod(access, name, desc, signature, exceptions);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
