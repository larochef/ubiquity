package org.ubiquity.util;

import org.ubiquity.CollectionFactory;

import java.util.*;

/**
 * Simple collection factory that should be fine for most cases.
 *
 * Date: 24/04/12
 *
 * @author François LAROCHE
 */
public enum DefaultCollectionFactory implements CollectionFactory {
    INSTANCE;

    @Override
    public <T> List<T> newList() {
        return new ArrayList<T>();
    }

    @Override
    public <T> Set<T> newSet() {
        return new HashSet<T>();
    }

    @Override
    public <K, T> Map<K, T> newMap() {
        return new HashMap<K, T>();
    }

    @Override
    public <T> Collection<T> newCollection() {
        return new ArrayList<T>();
    }
}
