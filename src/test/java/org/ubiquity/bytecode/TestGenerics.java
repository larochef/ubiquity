package org.ubiquity.bytecode;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 04/06/12
 *
 * @author François LAROCHE
 */
public class TestGenerics {

    public static class ParentClass<T> {
        private T parent;

        public T getParent() {
            return parent;
        }

        public void setParent(T parent) {
            this.parent = parent;
        }
    }

    public static class TestClass {
        private ParentClass<ParentClass> element;

        public ParentClass<ParentClass> getElement() {
            return element;
        }

        public void setElement(ParentClass<ParentClass> element) {
            this.element = element;
        }
    }

    @Test
    public void testPrinting() throws Exception {
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/TestGenerics$ParentClass");
        reader.accept(new PrintClassTest.MyVisitor(new TraceClassVisitor(new PrintWriter(System.out))), 0);
    }

    @Test
    public void testPrinting2() throws Exception {
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/TestGenerics$TestClass");
        reader.accept(new PrintClassTest.MyVisitor(new TraceClassVisitor(new PrintWriter(System.out))), 0);
    }

    @Test
    public void testPrinting3() throws Exception {
        Map<String, String> generics = new HashMap<String, String>();
        generics.put("T", "Lorg/ubiquity/bytecode/TestGenerics$ParentClass;");
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/TestGenerics$ParentClass");
        reader.accept(new GenericsVisitor(new PrintClassTest.MyVisitor(new TraceClassVisitor(new PrintWriter(System.out))), generics), 0);
    }
}
