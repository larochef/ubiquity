package org.ubiquity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Factory for the different kinds of common collections.
 * Implementing this interface allows you to choose the implementations.
 *
 * Date: 24/04/12
 *
 * @author François LAROCHE
 */
public interface CollectionFactory {

    <T> List <T> newList();

    <T> Set<T> newSet();

    <K,T> Map<K,T> newMap();
}
