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

    /**
     * Create a new List
     * @param <T>  the type of objects that can be added to the list
     * @return a new list, for objects of type T
     */
    <T> List <T> newList();

    /**
     * Create a new set
     * @param <T>  the type of objects that can be added to the set
     * @return a new set, for objects of type T
     */
    <T> Set<T> newSet();

    /**
     * Creates a new map
     *
     * @param <K> the type associated to the keys
     * @param <T> the type associated to the objects
     * @return a new map
     */
    <K,T> Map<K,T> newMap();
}
