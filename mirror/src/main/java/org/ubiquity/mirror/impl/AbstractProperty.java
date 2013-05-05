package org.ubiquity.mirror.impl;

import org.ubiquity.mirror.Property;

/**
 * Date: 05/05/13
 *
 * @author François LAROCHE
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
}
