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
package org.ubiquity.mirror.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.ubiquity.mirror.Function;
import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.Property;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public final class MapMirror implements Mirror<Map> {

    @Override
    public <U> Property<Map, U> getProperty(String name) {
        return (Property<Map, U>) new MapProperty<Object>(name, Object.class);
    }

    @Override
    public Collection<Property<Map, ?>> listProperties() {
        return ImmutableList.of();
    }

//    @Override
//    public <U> Function<Map, U> getFunction(String name) {
//        throw new UnsupportedOperationException("Not implemented");
//    }
}
