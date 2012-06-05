package org.ubiquity.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used as a key for builders. It contains the source, destinations classes,
 * but also a map describing the generics used.
 *
 * Date: 05/06/12
 * Time: 09:31
 *
 * @author françois LAROCHE
 */
public class CopierKey {

    public static Builder newBuilder(Class<?> source, Class<?> destination) {
        return new Builder(source, destination);
    }

    private final Class<?> sourceClass;
    private final Class<?> destinationClass;
    private final Map<String, String> sourceAnnotations;
    private final Map<String, String> destinationAnnotations;
    private final int hashCode;

    CopierKey(Builder builder) {
        this.sourceClass = builder.sourceClass;
        this.destinationClass = builder.destinationClass;
        this.sourceAnnotations = Collections.unmodifiableMap(builder.sourceAnnotations);
        this.destinationAnnotations = Collections.unmodifiableMap(builder.destinationAnnotations);
        this.hashCode = generateHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CopierKey copierKey = (CopierKey) o;

        if (destinationAnnotations != null ? !destinationAnnotations.equals(copierKey.destinationAnnotations) : copierKey.destinationAnnotations != null)
            return false;
        if (destinationClass != null ? !destinationClass.equals(copierKey.destinationClass) : copierKey.destinationClass != null)
            return false;
        if (sourceAnnotations != null ? !sourceAnnotations.equals(copierKey.sourceAnnotations) : copierKey.sourceAnnotations != null)
            return false;
        if (sourceClass != null ? !sourceClass.equals(copierKey.sourceClass) : copierKey.sourceClass != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    private int generateHashCode() {
        int result = sourceClass != null ? sourceClass.hashCode() : 0;
        result = 31 * result + (destinationClass != null ? destinationClass.hashCode() : 0);
        result = 31 * result + (sourceAnnotations != null ? sourceAnnotations.hashCode() : 0);
        result = 31 * result + (destinationAnnotations != null ? destinationAnnotations.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private final Class<?> sourceClass;
        private final Class<?> destinationClass;
        private final Map<String, String> sourceAnnotations;
        private final Map<String, String> destinationAnnotations;

        public Builder(Class<?> sourceClass, Class<?> destinationClass) {
            this.sourceAnnotations = new HashMap<String, String>();
            this.destinationAnnotations = new HashMap<String, String>();
            this.sourceClass = sourceClass;
            this.destinationClass = destinationClass;
        }

        public Builder sourceAnnotation(String name, String value) {
            this.sourceAnnotations.put(name, value);
            return this;
        }

        public Builder sourceAnnotations(Map<String, String> annotations) {
            this.sourceAnnotations.putAll(annotations);
            return this;
        }

        public Builder destinationAnnotation(String name, String value) {
            this.destinationAnnotations.put(name, value);
            return this;
        }

        public Builder destinationAnnotations(Map<String, String> annotations) {
            this.destinationAnnotations.putAll(annotations);
            return this;
        }

        public CopierKey build() {
            return new CopierKey(this);
        }
    }
}
