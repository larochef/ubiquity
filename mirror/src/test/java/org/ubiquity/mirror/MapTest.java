package org.ubiquity.mirror;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.mirror.util.Mirrors;

import java.util.Map;

/**
 */
public class MapTest {

    @Test
    public void testMaps() {
        Map<String, Object> map = Maps.newHashMap();
        Mirror mirror = Mirrors.newMirrorFactory().getMirror(map.getClass());
        Property<Map, Object> property = mirror.getProperty("property");
        property.set(map, "test");
        Assert.assertEquals("test", map.get("property"));
        Assert.assertEquals("test", property.get(map));
    }

}
