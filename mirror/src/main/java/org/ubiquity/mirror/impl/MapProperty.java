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
package org.ubiquity.mirror.impl;

import com.google.common.collect.ImmutableList;
import org.ubiquity.mirror.Annotation;
import org.ubiquity.mirror.Property;

import java.util.List;
import java.util.Map;

/**
 * Property used for Maps
 */
public final class MapProperty<T> implements Property<Map, T> {

    private final String propertyName;
    private final Class<T> valueClass;

    public MapProperty(String propertyName, Class<T> valueClass) {
        this.propertyName = propertyName;
        this.valueClass = valueClass;
    }

    @Override
    public T get(Map object) {
        return valueClass.cast(object.get(propertyName));
    }

    @Override
    public void set(Map object, T value) {
        object.put(propertyName, value);
    }

    @Override
    public Class<T> getWrappedClass() {
        return valueClass;
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public String getName() {
        return propertyName;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return ImmutableList.of();
    }
}
