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
 * Annotation allowing multiple configuration for the same property.
 *
 * Date: 25/05/12
 *
 * @author François LAROCHE
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CopyRenames {

    /**
     * The different property configurations for the target property
     *
     * @return the different property configurations for the target property
     */
    public CopyRename[] configurations();

}
