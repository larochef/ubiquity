package org.ubiquity.mirror.util;

import org.ubiquity.mirror.MirrorFactory;
import org.ubiquity.mirror.impl.MirrorFactoryImpl;
import org.ubiquity.util.SimpleClassLoader;

/**
 * Helper class, with static factories
 *
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public final class Mirrors {
    private Mirrors() {
        // Do not instanciate
    }

    public static MirrorFactory newMirrorFactory() {
        return newMirrorFactory(new SimpleClassLoader());
    }

    public static MirrorFactory newMirrorFactory(SimpleClassLoader loader) {
        return new MirrorFactoryImpl(loader);
    }


}
