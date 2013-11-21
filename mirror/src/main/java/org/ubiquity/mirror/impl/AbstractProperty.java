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

/**
 * Abstract property, to make bytecode generation more simple
 */
public abstract class AbstractProperty<T, U> implements Property<T, U> {

    private final String name;
    private final Class<U> wrappedClass;

    protected AbstractProperty(String name, Class<U> wrappedClass) {
        this.name = name;
        this.wrappedClass = wrappedClass;
    }

    @Override
    public U get(T object) {
        throw new UnsupportedOperationException(
                "Unable to get property " + name + ", check the readable flag before calling this method.");
    }

    @Override
    public boolean isReadable() {
        return false;
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public void set(T object, U value) {
        throw new UnsupportedOperationException(
                "Unable to set property, " + name + "check the writable flag before calling this method.");
    }

    public String getName() {
        return name;
    }

    @Override
    public Class<U> getWrappedClass() {
        return this.wrappedClass;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return ImmutableList.of();
    }
}
