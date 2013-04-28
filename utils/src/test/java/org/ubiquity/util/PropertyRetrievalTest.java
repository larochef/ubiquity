package org.ubiquity.util;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.ubiquity.util.objects.MyObject;

import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class PropertyRetrievalTest {

    @Test
    public void testPropertyParse() throws Exception {
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        ClassReader reader = new ClassReader(MyObject.class.getName());
        reader.accept(visitor, 0);
        Map<String,BytecodeProperty> properties = visitor.getProperties();
        Assert.assertEquals(properties.keySet().size(), 3);
        Assert.assertTrue(properties.containsKey("property1"));
        Assert.assertTrue(properties.containsKey("property2"));
        Assert.assertTrue(properties.containsKey("property3"));

        BytecodeProperty property1 = properties.get("property1");
        Assert.assertEquals(property1.getAnnotations().size(), 2);
        Iterator<Annotation> annotationIterator = property1.getAnnotations().iterator();
        Annotation annotation1 = annotationIterator.next();
        Assert.assertEquals(annotation1.getClazz(), "Lorg/ubiquity/util/objects/WithArguments;");
        Map<String, AnnotationProperty> properties1 = annotation1.getProperties();
        Assert.assertEquals(properties1.size(), 2);
        Assert.assertTrue(properties1.containsKey("stringValue"));
        Assert.assertTrue(properties1.containsKey("enumValue"));
        AnnotationProperty stringValue = properties1.get("stringValue");
        Assert.assertEquals(stringValue.getValue(), "toto");
        AnnotationProperty enumValue = properties1.get("enumValue");
        Assert.assertEquals(enumValue.getValue(), "VALUE1");

        Annotation annotation2 = annotationIterator.next();
        Assert.assertEquals(annotation2.getClazz(), "Lorg/ubiquity/util/objects/WithSimpleArray;");
        Map<String, AnnotationProperty> properties2 = annotation2.getProperties();
        Assert.assertEquals(properties2.entrySet().size(), 1);
        Assert.assertArrayEquals((Object[]) properties2.get("value").getValue(),
                new Object[] {"value1", "value2"});


        BytecodeProperty property2 = properties.get("property2");
        Assert.assertEquals(property2.getAnnotations().size(), 1);

        BytecodeProperty property3 = properties.get("property3");
        Assert.assertTrue(property3.getAnnotations().isEmpty());

    }

}
