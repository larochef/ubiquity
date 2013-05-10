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

import java.util.Collection;

/**
 * Class handling a class. it can get properties and functions
 */
public interface Mirror<T> {

    /**
     * Retrieves a named property for a class
     *
     * @param name the name of the property
     * @param <U> the desired type of property value, users must know what to expect to avoid later exceptions
     * @return the named property if it exists, {@code null} else
     */
    <U> Property<T, U> getProperty(String name);

    /**
     * List the different properties available for this mirror
     *
     * @return the list of compatible properties
     */
    Collection<Property<T, ?>> listProperties();

//    <U> Function<T, U> getFunction(String name);

}
