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
