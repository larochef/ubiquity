package org.ubiquity.util;

import org.ubiquity.CollectionFactory;

import java.util.*;

/**
 * Date: 24/04/12
 *
 * @author François LAROCHE
 */
public class DefaultCollectionFactory implements CollectionFactory {

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
}
