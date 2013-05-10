package org.ubiquity.mirror.impl;

import org.ubiquity.mirror.Property;

import java.util.Map;

/**
 *
 */
public class MapProperty<T> implements Property<Map, T> {

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
}
