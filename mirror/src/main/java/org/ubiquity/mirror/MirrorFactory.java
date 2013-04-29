package org.ubiquity.mirror;

/**
 * Interface defining mirrors retrieval, for mocking utilities
 *
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public interface MirrorFactory {

    <T> Mirror<T> getMirror(Class<T> requestedClass);

}
