package org.ubiquity.mirror;

import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.mirror.objects.BasicAnnotation;
import org.ubiquity.mirror.objects.SimpleAnnotatedObject;
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

}
