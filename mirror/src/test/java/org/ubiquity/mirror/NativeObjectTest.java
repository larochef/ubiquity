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
