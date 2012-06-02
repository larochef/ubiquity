package org.ubiquity.bytecode;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.ubiquity.Copier;

import java.io.IOException;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 * Date: 02/06/12
 *
 * @author François LAROCHE
 */
public class TestAnnotations {

    @Test
    public void testAnnotationsParsing() throws IOException {
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/AnnotatedClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
        Map<String, Property> properties = visitor.getProperties();
        assertFalse(properties.isEmpty());
        assertTrue(properties.containsKey("property1"));
        Property property1 = properties.get("property1");
        assertFalse(property1.getAnnotations().isEmpty());
        assertTrue(property1.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:property3:Lorg/ubiquity/bytecode/SimpleTestClass;"));

        Property property3 = properties.get("property3");
        assertNotNull(property3);
        assertFalse(property3.getAnnotations().isEmpty());
        assertTrue(property3.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:test:*"));
        assertTrue(property3.getAnnotations().contains("Lorg/ubiquity/annotation/CopyRename;:property3:Lorg/ubiquity/bytecode/SimpleTestClass;"));
    }

    @Test
    public void testSimpleRenaming() throws Exception {
        AnnotatedClass2 object = new AnnotatedClass2();
        object.setToto("toto");


        CopyContext ctx = new CopyContext();
        Copier<AnnotatedClass2, SimpleTestClass> copier =
                new CopierGenerator().createCopier(AnnotatedClass2.class, SimpleTestClass.class, ctx);

        SimpleTestClass dest = copier.map(object);
        assertNotNull(dest);
        assertEquals("toto", dest.getProperty1());
    }
}
