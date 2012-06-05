package org.ubiquity.bytecode;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.ubiquity.Ubiquity;
import org.ubiquity.annotation.CopyRename;
import org.ubiquity.annotation.CopyRenames;

import java.io.IOException;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Date: 02/06/12
 *
 * @author François LAROCHE
 */
public class TestAnnotations {

    private static final Ubiquity ubiquity = new Ubiquity();

    public static class SimpleTestClass {

        private String property1;
        private String property3;

        public String getProperty1() {
            return property1;
        }
        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public String getProperty3() {
            return property3;
        }

        public void setProperty3(String property3) {
            this.property3 = property3;
        }
    }


    public static class AnnotatedClass {

        private String property1;
        private String property2;
        private String property3;

        @CopyRename(targetClass = SimpleTestClass.class, propertyName = "property3")
        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        @CopyRename(propertyName = "test")
        public String getProperty2() {
            return property2;
        }

        public void setProperty2(String property2) {
            this.property2 = property2;
        }

        @CopyRenames(configurations = {@CopyRename(propertyName = "test"),@CopyRename(propertyName = "property3", targetClass = SimpleTestClass.class)})
        public String getProperty3() {
            return property3;
        }

        public void setProperty3(String property3) {
            this.property3 = property3;
        }
    }

    public static class AnnotatedClass2 {
        private String toto;

        @CopyRename(propertyName = "property1")
        public String getToto() {
            return toto;
        }

        public void setToto(String toto) {
            this.toto = toto;
        }
    }


    @Test
    public void testAnnotationsParsing() throws IOException {
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/TestAnnotations$AnnotatedClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
        Map<String, Property> properties = visitor.getProperties();
        assertFalse(properties.isEmpty());
        assertTrue(properties.containsKey("property1"));
        Property property1 = properties.get("property1");
        assertFalse(property1.getAnnotations().isEmpty());
        assertTrue(property1.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:property3:Lorg/ubiquity/bytecode/TestAnnotations$SimpleTestClass;"));

        Property property3 = properties.get("property3");
        assertNotNull(property3);
        assertFalse(property3.getAnnotations().isEmpty());
        assertTrue(property3.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:test:*"));
        assertTrue(property3.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:property3:Lorg/ubiquity/bytecode/TestAnnotations$SimpleTestClass;"));
    }

    @Test
    public void testSimpleRenaming() throws Exception {
        AnnotatedClass2 object = new AnnotatedClass2();
        object.setToto("toto");

        SimpleTestClass dest = ubiquity.map(object, SimpleTestClass.class);
        assertNotNull(dest);
        assertEquals("toto", dest.getProperty1());
    }
}
