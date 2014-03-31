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
package org.ubiquity.util;

import org.junit.Assert;

/**
 * Test for the {@link ByteCodeStringHelper} class
 */
public class ByteCodeStringHelperTest {

    @org.junit.Test
    public void testClassRetrieval() {
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("B"), byte.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("C"), char.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("D"), double.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("F"), float.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("I"), int.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("J"), long.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("S"), short.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("Z"), boolean.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("java/lang/Object"), Object.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("Ljava/lang/Object;"), Object.class);
        Assert.assertSame(ByteCodeStringHelper.toJavaClass("[Ljava/lang/Object;"), Object[].class);

    }

}
