package org.ubiquity.mirror;

import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.mirror.objects.BasicAnnotation;
import org.ubiquity.mirror.objects.SimpleAnnotatedObject;
import org.ubiquity.mirror.util.Mirrors;

import java.util.List;

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
        Assert.assertTrue(annotation.getProperties().isEmpty());
    }

}
