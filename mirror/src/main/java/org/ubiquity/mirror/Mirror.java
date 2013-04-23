package org.ubiquity.mirror;

/**
 *
 *
 * Date: 13/04/13
 *
 * @author François LAROCHE
 */
public interface Mirror<T> {

    Property<T, ?> getProperty(String name);

    Function<T, ?> getFunction(String name);

}
