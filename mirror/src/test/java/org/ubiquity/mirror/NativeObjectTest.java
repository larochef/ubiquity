package org.ubiquity.mirror;

import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.mirror.objects.NativeObject;
import org.ubiquity.mirror.util.Mirrors;

/**
 *
 */
public class NativeObjectTest {

    @Test
    public void testInt() {
        Mirror<NativeObject> mirror = Mirrors.newMirrorFactory().getMirror(NativeObject.class);
        NativeObject object = new NativeObject();

        Property<NativeObject, Integer> intProperty = mirror.getProperty("intValue");
        Integer value = 1000000;
        intProperty.set(object, value);
        Assert.assertTrue(1000000 == object.getIntValue());
        Integer result = intProperty.get(object);
        Assert.assertNotSame(value, result);
    }

}
