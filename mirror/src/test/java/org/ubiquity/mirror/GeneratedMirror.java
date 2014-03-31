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
package org.ubiquity.mirror;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.ubiquity.mirror.impl.AbstractMirror;
import org.ubiquity.mirror.impl.AbstractProperty;
import org.ubiquity.mirror.objects.BasicAnnotation;
import org.ubiquity.mirror.objects.ValueObject;

import java.util.Map;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public class GeneratedMirror extends AbstractMirror<ValueObject> {

    @Override
    protected Map<String, Property<ValueObject, ?>> buildProperties() {
        return ImmutableMap.<String, Property<ValueObject, ?>>builder().put("property1", new Property1())
                .put("property2", new Property2())
                .put("property3", new Property3()).build();
    }

    static class Property1 extends AbstractProperty<ValueObject, String> {

        public Object test() {
            ImmutableMap.Builder<String, AnnotationProperty> builder = ImmutableMap.builder();
            return new Annotation(BasicAnnotation.class, true, Maps.<String, AnnotationProperty>newHashMap());
        }

        public void printMe() {
            final int[] ints = {1, 2, 3};
            final String[] strings = new String [] {"a", "Z", "e"};
        }

        public Property1() {
            super("property1", String.class);
        }

        @Override
        public String get(ValueObject object) {
            return object.getProperty1();
        }

        @Override
        public void set(ValueObject object, String value) {
            object.setProperty1(value);
        }

        @Override
        public boolean isReadable() {
            return true;
        }

        @Override
        public boolean isWritable() {
            return true;
        }
    }

    static class Property2 extends AbstractProperty<ValueObject, String> {
        public Property2() {
            super("property2", String.class);
        }

        @Override
        public String get(ValueObject object) {
            return object.getProperty2();
        }

        @Override
        public void set(ValueObject object, String value) {
            object.setProperty2(value);
        }

        @Override
        public boolean isReadable() {
            return true;
        }

        @Override
        public boolean isWritable() {
            return true;
        }
    }

    static class Property3 extends AbstractProperty<ValueObject, String> {
        public Property3() {
            super("property3", String.class);
        }

        @Override
        public String get(ValueObject object) {
            return object.getProperty3();
        }

        @Override
        public void set(ValueObject object, String value) {
            object.setProperty3(value);
        }

        @Override
        public boolean isReadable() {
            return true;
        }

        @Override
        public boolean isWritable() {
            return true;
        }
    }
}
