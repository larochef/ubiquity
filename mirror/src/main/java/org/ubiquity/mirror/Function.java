package org.ubiquity.mirror;

/**
 * Date: 13/04/13
 *
 * @author François LAROCHE
 */
public interface Function<T, U> {

    U invoke(T object, Object... parameters);

    Class<U> getWrappedClass();

    Class [] getArgumentClasses();
}
