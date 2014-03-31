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
package org.ubiquity.copy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to rename a property to a target class.
 *
 * If no target class is specified, then the rename will apply to any target class.
 * This annotation must be placed on a getter or a setter.
 *
 * Date: 25/05/12
 *
 * @author François LAROCHE
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CopyRename {
    /**
     * The name of the destination property
     *
     * @return The name of the destination property
     */
    String propertyName();

    /**
     * The class of the objects matching this rename rule
     * If Object.class is returned, then this rule will apply to any un-configured rule.
     * This property can be safely omitted.
     *
     * @return the class matching this rule
     */
    Class<?> targetClass() default Object.class;
}
