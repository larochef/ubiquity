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
 * Test for the {@link ByteCodeStringUtils} class
 */
public class ByteCodeStringUtilsTest {

    @org.junit.Test
    public void testClassRetrieval() {
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("B"), Byte.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("C"), Character.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("D"), Double.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("F"), Float.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("I"), Integer.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("L"), Long.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("S"), Short.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("Z"), Boolean.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("java/lang/Object"), Object.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("Ljava/lang/Object;"), Object.class);
        Assert.assertSame(ByteCodeStringUtils.toJavaClass("[Ljava/lang/Object;"), Object[].class);

    }

}
