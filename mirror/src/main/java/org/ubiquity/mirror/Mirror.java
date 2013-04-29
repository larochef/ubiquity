package org.ubiquity.mirror;

/**
 *
 *
 * Date: 13/04/13
 *
 * @author François LAROCHE
 */
public interface Mirror<T> {

    <U> Property<T, U> getProperty(String name);

    <U> Function<T, U> getFunction(String name);

}
