package org.ubiquity.mirror;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;
import org.ubiquity.mirror.impl.MirrorGenerator;
import org.ubiquity.mirror.objects.*;
import org.ubiquity.mirror.util.Mirrors;
import org.ubiquity.util.NativeConverter;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 */
public class AnnotationTest {

    @Test
    public void testPresentAnnotation() {
        Mirror<SimpleAnnotatedObject> mirror = Mirrors.newMirrorFactory().getMirror(SimpleAnnotatedObject.class);
        List<Annotation> annotations = mirror.listProperties().iterator().next().getAnnotations();
        Assert.assertEquals(annotations.size(), 1);
        Annotation annotation = annotations.iterator().next();
        Assert.assertEquals(annotation.getAnnotationClass(), BasicAnnotation.class);
        Assert.assertEquals(annotation.getProperties().size(), 1);
        Map.Entry<String, AnnotationProperty> entry = annotation.getProperties().entrySet().iterator().next();
        Assert.assertEquals(entry.getKey(), "value");
        Assert.assertEquals(entry.getValue().getValue(), "testMe");
    }

    @Test
    public void testComplexAnnotations() {
        Mirror<ComplexAnnotatedObject> mirror = Mirrors.newMirrorFactory().getMirror(ComplexAnnotatedObject.class);
        Property<ComplexAnnotatedObject, Object> field1 = mirror.getProperty("field1");
        Assert.assertEquals(field1.getAnnotations().size(), 1);
        Annotation annotation1 = field1.getAnnotations().iterator().next();
        Assert.assertSame(annotation1.getAnnotationClass(), NativeAnnotation.class);
        Map<String, AnnotationProperty> properties = annotation1.getProperties();
        Assert.assertEquals(properties.size(), 8);
        Assert.assertEquals(properties.get("byteValue").getValue(), Byte.valueOf("1"));
        Assert.assertEquals(properties.get("charValue").getValue(), Character.valueOf ((char) 2));
        Assert.assertEquals(properties.get("doubleValue").getValue(), Double.valueOf(3));
        Assert.assertEquals(properties.get("floatValue").getValue(), Float.valueOf(4));
        Assert.assertEquals(properties.get("intValue").getValue(), Integer.valueOf(5));
        Assert.assertEquals(properties.get("longValue").getValue(), Long.valueOf(6));
        Assert.assertEquals(properties.get("shortValue").getValue(), Short.valueOf((short) 7));
        Assert.assertEquals(properties.get("booleanValue").getValue(), Boolean.valueOf(true));

        Property<ComplexAnnotatedObject, Object> field2 = mirror.getProperty("field2");

    }

    @Test
    public void testEnumAnnotations() {
        Mirror<AnnotatedEnumObject> mirror = Mirrors.newMirrorFactory().getMirror(AnnotatedEnumObject.class);
        final Property<AnnotatedEnumObject, Object> property1 = mirror.getProperty("property1");
        final List<Annotation> annotations = property1.getAnnotations();
        Assert.assertEquals(1, annotations.size());
        final Map<String, AnnotationProperty> propertyMap = annotations.iterator().next().getProperties();
        Assert.assertEquals(1, propertyMap.size());
        Assert.assertTrue(propertyMap.containsKey("value"));
        final AnnotationProperty value = propertyMap.get("value");
        Assert.assertSame(TestEnum.VALUE1, value.getValue());


    }
}
