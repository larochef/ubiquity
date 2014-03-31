/*
 * Copyright 2012 ubiquity-copy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ubiquity.mirror;

import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.mirror.objects.*;
import org.ubiquity.mirror.util.Mirrors;

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

    @Test
    public void testAnnotatedAnnotations() {
        Mirror<AnnotatedAnnotationObject> mirror = Mirrors.newMirrorFactory().getMirror(AnnotatedAnnotationObject.class);
        final List<Annotation> annotations = mirror.getProperty("property").getAnnotations();
        Assert.assertEquals(1, annotations.size());
        final Annotation annotation1 = annotations.iterator().next();
        final AnnotationProperty annotationProperty = annotation1.getProperties().get("value");
        final Annotation annotation2 = (Annotation) annotationProperty.getValue();
        Assert.assertEquals("Annotation !", annotation2.getProperties().get("value").getValue());
    }

    @Test
    public void testArrayAnnotations() {
        Mirror<ArrayAnnotatedObject> mirror = Mirrors.newMirrorFactory().getMirror(ArrayAnnotatedObject.class);
        final List<Annotation> annotations = mirror.getProperty("property").getAnnotations();
        Assert.assertEquals(1, annotations.size());
        final Annotation annotation = annotations.iterator().next();
        final AnnotationProperty annotationProperty = annotation.getProperties().get("stringValue");
        String[] value = (String[]) annotationProperty.getValue();
        Assert.assertArrayEquals(new String[] {"1", "2", "3"}, value);
    }

    @Test
    public void testArrayOfAnnotationsAnnotations() {
        Mirror<AnnotatedArrayObject> mirror = Mirrors.newMirrorFactory().getMirror(AnnotatedArrayObject.class);
        final List<Annotation> annotations = mirror.getProperty("property").getAnnotations();
        Assert.assertEquals(1, annotations.size());
        final Annotation annotation = annotations.iterator().next();
        final AnnotationProperty annotationProperty = annotation.getProperties().get("values");
        final Annotation[] value = (Annotation[]) annotationProperty.getValue();
        Assert.assertEquals(2, value.length);
        Assert.assertSame(BasicAnnotation.class, value[0].getAnnotationClass());
        Assert.assertSame(BasicAnnotation.class, value[1].getAnnotationClass());
        Assert.assertEquals("Annotation 1", value[0].getProperties().get("value").getValue());
        Assert.assertEquals("Annotation 2", value[1].getProperties().get("value").getValue());
    }

    @Test
    public void testArrayOfEnumAnnotations() {
        Mirror<ArrayOfEnumAnnotationObject> mirror = Mirrors.newMirrorFactory().getMirror(ArrayOfEnumAnnotationObject.class);
        final List<Annotation> annotations = mirror.getProperty("property").getAnnotations();
        Assert.assertEquals(1, annotations.size());
        final Annotation annotation = annotations.iterator().next();
        final AnnotationProperty annotationProperty = annotation.getProperties().get("values");
        final TestEnum[] value = (TestEnum[]) annotationProperty.getValue();
        Assert.assertEquals(2, value.length);
        Assert.assertSame(TestEnum.VALUE1, value[0]);
        Assert.assertSame(TestEnum.VALUE2, value[1]);
    }

}
