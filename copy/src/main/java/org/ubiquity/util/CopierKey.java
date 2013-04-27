package org.ubiquity.util;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

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
public final class CopierKey <T, U> {

    public static <A, B> Builder<A,B> newBuilder(Class<A> source, Class<B> destination) {
        return new Builder<A, B>(source, destination);
    }

    public static <A, B> CopierKey<A, B> newKey(Class<A> source, Class<B> destination) {
        return new CopierKey<A, B>(source, destination, ImmutableMap.<String, String>of(),
                ImmutableMap.<String, String>of());
    }

    private final Class<T> sourceClass;
    private final Class<U> destinationClass;
    private final Map<String, String> sourceAnnotations;
    private final Map<String, String> destinationAnnotations;
    private final int hashCode;

    CopierKey(Class<T> sourceClass, Class<U> destinationClass, Map<String, String> sourceAnnotations,
              Map<String, String> destinationAnnotations) {
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
        this.sourceAnnotations = ImmutableMap.copyOf(sourceAnnotations);
        this.destinationAnnotations = ImmutableMap.copyOf(destinationAnnotations);
        this.hashCode = Objects.hashCode(this.sourceClass, this.destinationClass, this.sourceAnnotations,
                this.destinationAnnotations);
    }

    public Class<T> getSourceClass() {
        return sourceClass;
    }

    public Class<U> getDestinationClass() {
        return destinationClass;
    }

    public Map<String, String> getSourceAnnotations() {
        return sourceAnnotations;
    }

    public Map<String, String> getDestinationAnnotations() {
        return destinationAnnotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CopierKey<?,?> copierKey = (CopierKey<?,?>) o;

        return sourceClass == copierKey.sourceClass &&
               destinationClass == copierKey.destinationClass &&
               Objects.equal(sourceAnnotations, copierKey.sourceAnnotations) &&
               Objects.equal(destinationAnnotations, copierKey.destinationAnnotations);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }


    public static class Builder <T,U> {
        private final Class<T> sourceClass;
        private final Class<U> destinationClass;
        private final Map<String, String> sourceAnnotations;
        private final Map<String, String> destinationAnnotations;

        Builder(Class<T> sourceClass, Class<U> destinationClass) {
            this.sourceAnnotations = new HashMap<String, String>();
            this.destinationAnnotations = new HashMap<String, String>();
            this.sourceClass = sourceClass;
            this.destinationClass = destinationClass;
        }

        public Builder<T,U> sourceAnnotation(String name, String value) {
            this.sourceAnnotations.put(name, value);
            return this;
        }

        public Builder<T,U> sourceAnnotations(Map<String, String> annotations) {
            this.sourceAnnotations.putAll(annotations);
            return this;
        }

        public Builder<T,U> destinationAnnotation(String name, String value) {
            this.destinationAnnotations.put(name, value);
            return this;
        }

        public Builder<T,U> destinationAnnotations(Map<String, String> annotations) {
            this.destinationAnnotations.putAll(annotations);
            return this;
        }

        public CopierKey<T, U> build() {
            return new CopierKey<T,U>(this.sourceClass, this.destinationClass, this.sourceAnnotations,
                    this.destinationAnnotations);
        }
    }
}
