package org.ubiquity.mirror.impl;

import org.ubiquity.mirror.Function;
import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.Property;

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
    public <U> Function<Map, U> getFunction(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
