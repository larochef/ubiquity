package org.ubiquity.mirror.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.MirrorFactory;
import org.ubiquity.util.SimpleClassLoader;

import java.util.concurrent.ExecutionException;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public class MirrorFactoryImpl implements MirrorFactory {

    LoadingCache<Class<?>, Mirror<?>> mirrorCache;

    private final SimpleClassLoader loader;

    public MirrorFactoryImpl(SimpleClassLoader loader) {
        this.loader = loader;
        this.mirrorCache = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, Mirror<?>>() {
            @Override
            public Mirror<?> load(Class<?> aClass) throws Exception {
                // TODO : implement.me
                return null;
            }
        });
    }

    @Override
    public <T> Mirror<T> getMirror(Class<T> requestedClass) {
        try {
            return (Mirror<T>) mirrorCache.get(requestedClass);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
