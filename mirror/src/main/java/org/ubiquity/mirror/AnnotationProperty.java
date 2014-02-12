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

import com.google.common.base.Objects;

/**
 */
public final class AnnotationProperty<T> {

    private final String name;
    private final Class<T> valueClass;
    private final T value;
    private final int hash;

    public AnnotationProperty(String name, Class<T> valueClass, T value) {
        this.name = name;
        this.valueClass = valueClass;
        this.value = value;
        this.hash = Objects.hashCode(name, valueClass, value);
    }

    public String getName() {
        return name;
    }

    public Class<T> getValueClass() {
        return valueClass;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationProperty other = (AnnotationProperty) obj;
        return Objects.equal(this.name, other.name)
                && Objects.equal(this.valueClass, other.valueClass)
                && Objects.equal(this.value, other.value);
    }
}
