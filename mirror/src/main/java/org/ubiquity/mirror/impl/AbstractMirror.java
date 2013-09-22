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

import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.Property;

import java.util.Collection;
import java.util.Map;

/**
 * Abstract implementation for mirrors, to make bytecode generation easier.
 */
public abstract class AbstractMirror<T> implements Mirror<T> {

    private final Map<String, Property<T, ?>> properties = buildProperties();

    @Override
    @SuppressWarnings("unchecked") // syntaxic sugar, callers should know what to expect.
    public <U> Property<T, U> getProperty(String name) {
        return (Property<T, U>) properties.get(name);
    }

    @Override
    public Collection<Property<T, ?>> listProperties() {
        return properties.values();
    }

//    @Override
//    public <U> Function<T, U> getFunction(String name) {
//        throw new UnsupportedOperationException("Not implemented yet");
//    }

    protected abstract Map<String, Property<T, ?>> buildProperties();

}
