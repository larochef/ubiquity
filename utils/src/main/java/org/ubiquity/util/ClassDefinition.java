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
package org.ubiquity.util;

import com.google.common.base.Objects;

/**
 *
 */
public final class ClassDefinition {

    private final String className;
    private final byte [] classContent;
    private final int cachedHashCode;

    public ClassDefinition(String className, byte[] classContent) {
        this.className = className;
        this.classContent = classContent;
        this.cachedHashCode = Objects.hashCode(this.className, this.classContent);
    }

    public String getClassName() {
        return className;
    }

    public byte[] getClassContent() {
        return classContent;
    }

    @Override
    public int hashCode() {
        return cachedHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ClassDefinition other = (ClassDefinition) obj;
        return Objects.equal(this.className, other.className)
                && Objects.equal(this.classContent, other.classContent);

    }
}
