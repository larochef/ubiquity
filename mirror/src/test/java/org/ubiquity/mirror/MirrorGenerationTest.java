package org.ubiquity.mirror;

import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.mirror.objects.ValueObject;
import org.ubiquity.mirror.util.Mirrors;

/**
 *
 */
public class MirrorGenerationTest {

    @Test
    public void testGeneration() {

        Mirror<ValueObject> mirror = Mirrors.newMirrorFactory().getMirror(ValueObject.class);
        Assert.assertNotNull(mirror);

        ValueObject object = new ValueObject();
        object.setProperty1("value1");
        object.setProperty2("value2");
        object.setProperty3("value3");

        Assert.assertNotNull(mirror.getProperty("property1"));
        Assert.assertNotNull(mirror.getProperty("property2"));
        Assert.assertNotNull(mirror.getProperty("property3"));

        Property<ValueObject, String> property1 = mirror.getProperty("property1");
        Property<ValueObject, String> property2 = mirror.getProperty("property2");
        Property<ValueObject, String> property3 = mirror.getProperty("property3");

        Assert.assertEquals("value1", property1.get(object));
        Assert.assertEquals("value2", property2.get(object));
        Assert.assertEquals("value3", property3.get(object));

        property1.set(object, "changedValue1");
        property2.set(object, "changedValue2");
        property3.set(object, "changedValue3");

        Assert.assertEquals("changedValue1", property1.get(object));
        Assert.assertEquals("changedValue2", property2.get(object));
        Assert.assertEquals("changedValue3", property3.get(object));
    }
}
