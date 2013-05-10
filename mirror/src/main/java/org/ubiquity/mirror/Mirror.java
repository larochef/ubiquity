package org.ubiquity.mirror;

/**
 *
 *
 * Date: 13/04/13
 *
 * @author François LAROCHE
 */
public interface Mirror<T> {

    <U extends Object> Property<T, U> getProperty(String name);

    <U extends Object> Function<T, U> getFunction(String name);

}
