package org.ubiquity.mirror.impl;

import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.MirrorFactory;
import org.ubiquity.util.SimpleClassLoader;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public class MirrorFactoryImpl implements MirrorFactory {
    private final SimpleClassLoader loader;

    public MirrorFactoryImpl(SimpleClassLoader loader) {
        this.loader = loader;
    }

    @Override
    public <T> Mirror<T> getMirror(Class<T> requestedClass) {
        return null;
    }
}
