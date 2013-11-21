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
package org.ubiquity.mirror;

import java.util.List;

/**
 * Interface used to manipulate a property.
 */
public interface Property<T, U> {

    /**
     * Retrieves the property value in a given object.
     * <b>this method is not null-safe</b>
     *
     * @param object the object for which to retrieve property
     * @return the value of the property for the given object
     * @throws NullPointerException if the given object is null
     * @throws UnsupportedOperationException if the property isn't readable
     */
    U get(T object);

    /**
     * Writes the value for a given object.
     * <b>this method is not null-safe</b>
     *
     * @param object the object in which to write the value
     * @param value the value to write
     * @throws NullPointerException if the given object is null
     * @throws UnsupportedOperationException if the property isn't writable
     */
    void set(T object, U value);

    /**
     * Returns the class of the property
     *
     * @return the class used in this property
     */
    Class<U> getWrappedClass();

    /**
     * States whether this property can be read
     *
     * @return whether or not this property can be read
     */
    boolean isReadable();

    /**
     * States whether this property can be written
     *
     * @return whether or not this property can be written
     */
    boolean isWritable();

    /**
     * Returns the name of this property
     *
     * @return the name of this property
     */
    String getName();

    /**
     * retrieves the annotations linked to this property
     *
     * @return the annotations from this property
     */
    List<Annotation> getAnnotations();

}
