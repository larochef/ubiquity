package org.ubiquity.bytecode;

import org.junit.Test;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.ubiquity.util.Constants;

import java.io.IOException;

/**
 * @author François LAROCHE
 */
public class PrintClassTest {
    @Test
    public void printCodeTest() throws IOException {
//        ClassReader reader = new ClassReader("org/ubiquity/bytecode/SimpleCopier");
//        reader.accept(new MyVisitor(new TraceClassVisitor(new PrintWriter(System.out))),0);
    }

    protected static class MyVisitor extends ClassVisitor {

        protected MyVisitor(ClassVisitor v) {
            super(Constants.ASM_LEVEL, v);
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
