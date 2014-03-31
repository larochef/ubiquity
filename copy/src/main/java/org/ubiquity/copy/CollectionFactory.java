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
package org.ubiquity.copy;

import java.util.Collection;
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

    /**
     * Default collection creation
     * @param <T> the type of elments to store in the collection
     * @return a new Collection for objects of type T
     */
    <T>Collection<T> newCollection();
}
