package org.ubiquity.mirror;

/**
 *
 *
 * Date: 13/04/13
 *
 * @author François LAROCHE
 */
public interface Property<T, U> {

    U get(T object);

    void set(T object, U value);

    Class<U> getWrappedClass();

    boolean isReadable();

    boolean isWritable();

}
