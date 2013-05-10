/**
 * Copyright 2012 ubiquity-copy

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.ubiquity.util;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.ubiquity.util.objects.MyObject;
import org.ubiquity.util.visitors.Annotation;
import org.ubiquity.util.visitors.AnnotationProperty;
import org.ubiquity.util.visitors.BytecodeProperty;
import org.ubiquity.util.visitors.PropertyRetrieverVisitor;

import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class PropertyRetrievalTest {

    @Test
    public void testPropertyParse() throws Exception {
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        ClassReader reader = new ClassReader(MyObject.class.getName());
        reader.accept(visitor, 0);
        Map<String,BytecodeProperty> properties = visitor.getProperties();
        Assert.assertEquals(properties.keySet().size(), 3);
        Assert.assertTrue(properties.containsKey("property1"));
        Assert.assertTrue(properties.containsKey("property2"));
        Assert.assertTrue(properties.containsKey("property3"));

        BytecodeProperty property1 = properties.get("property1");
        Assert.assertEquals(property1.getAnnotations().size(), 2);
        Iterator<Annotation> annotationIterator = property1.getAnnotations().iterator();
        Annotation annotation1 = annotationIterator.next();
        Assert.assertEquals(annotation1.getClazz(), "Lorg/ubiquity/util/objects/WithArguments;");
        Map<String, AnnotationProperty> properties1 = annotation1.getProperties();
        Assert.assertEquals(properties1.size(), 2);
        Assert.assertTrue(properties1.containsKey("stringValue"));
        Assert.assertTrue(properties1.containsKey("enumValue"));
        AnnotationProperty stringValue = properties1.get("stringValue");
        Assert.assertEquals(stringValue.getValue(), "toto");
        AnnotationProperty enumValue = properties1.get("enumValue");
        Assert.assertEquals(enumValue.getValue(), "VALUE1");

        Annotation annotation2 = annotationIterator.next();
        Assert.assertEquals(annotation2.getClazz(), "Lorg/ubiquity/util/objects/WithSimpleArray;");
        Map<String, AnnotationProperty> properties2 = annotation2.getProperties();
        Assert.assertEquals(properties2.entrySet().size(), 1);
        Assert.assertArrayEquals((Object[]) properties2.get("value").getValue(),
                new Object[] {"value1", "value2"});


        BytecodeProperty property2 = properties.get("property2");
        Assert.assertEquals(property2.getAnnotations().size(), 1);

        BytecodeProperty property3 = properties.get("property3");
        Assert.assertTrue(property3.getAnnotations().isEmpty());

    }

}
