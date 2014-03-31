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

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Date: 05/06/12
 *
 * @author François LAROCHE
 */
public class MapBuilder {
    private static final String SOURCE = "--src##";
    private static final String DEST = "$$src||";

    private final Map<String, Object> values = new HashMap<String, Object>();

    public MapBuilder generics(String name, String value) {
        this.values.put(name, value);
        return this;
    }

    public MapBuilder source(Class<?> source) {
        this.values.put(SOURCE, source);
        return this;
    }

    public MapBuilder destination(Class<?> destination) {
        this.values.put(DEST, destination);
        return this;
    }

    public Map<String, ?> build() {
        return ImmutableMap.copyOf(this.values);
    }


}
