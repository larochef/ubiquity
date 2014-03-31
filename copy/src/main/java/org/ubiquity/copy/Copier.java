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

import java.util.List;

/**
 * This interface defines a Copier, used to copy an object into another,
 * or map an object into another one of another class.
 *
 * @author François LAROCHE
 *
 * @param <T> the source object class
 * @param <U> the destination object class
 */
public interface Copier<T, U> {

    /**
     * Copies the values of the object given in a new object of the destination class.
     *
     * @param element the object to copy into a new destination object
     * @return a new object of the destination, in which have been copied the properties
     */
	U map(T element);

    /**
     * Copy a list of elements of the source class into a list of new elements of the destination class
     *
     * @param elements the source objects
     * @return objects of the destination class, containing the objects
     */
	List<U> map(List<T> elements);

    /**
     * Copy an object properties into another.
     *
     * @param source the source object, from which to read the properties
     * @param destination the destination object, in which to write the properties
     */
    void copy(T source, U destination);

    /**
     * Map an array of T elements to an array of U elements.
     * If the specified array of Us doesn't have the same size as the array of T
     * or is null, then a new array of U will be created.
     * Else, the original array will be returned.
     *
     * @param src the array containing the Ts to copy
     * @param target the array containing the destination Us
     * @return an array, that can be the target argument containing all the merged elements
     */
    U[] map (T[] src, U[] target);

}
