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
package org.ubiquity.util.visitors;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Class representing an annotation.
 */
public final class Annotation {

    private String clazz;
    private Map<String, AnnotationProperty> properties;
    boolean visible;

    public Annotation() {
        this.properties = Maps.newHashMap();
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Map<String, AnnotationProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, AnnotationProperty> properties) {
        this.properties = properties;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
