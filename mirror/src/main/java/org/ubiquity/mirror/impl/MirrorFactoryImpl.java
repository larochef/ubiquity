/*
 * Copyright 2012 ubiquity-copy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ubiquity.mirror.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.ubiquity.mirror.Mirror;
import org.ubiquity.mirror.MirrorFactory;
import org.ubiquity.util.ClassDefinition;
import org.ubiquity.util.SimpleClassLoader;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Default factory for mirrors
 */
public final class MirrorFactoryImpl implements MirrorFactory {

    private final LoadingCache<Class<?>, Mirror<?>> mirrorCache;

    final SimpleClassLoader loader;

    public MirrorFactoryImpl(final SimpleClassLoader loader) {
        this.loader = loader;
        this.mirrorCache = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, Mirror<?>>() {
            @Override
            public Mirror<?> load(Class<?> aClass) throws Exception {
                Collection<ClassDefinition> definitions = MirrorGenerator.generateMirror(aClass);
                Class<?> c = null;
                for(ClassDefinition def : definitions) {
                    c = loader.defineClass(def.getClassName().replace('/', '.'), def.getClassContent());
                }
                if(c != null) {
                    return (Mirror) c.newInstance();
                }
                return null;
            }
        });
        this.mirrorCache.put(Map.class, new MapMirror());
    }

    @Override
    public <T> Mirror<T> getMirror(Class<T> requestedClass) {
        try {
            Class<?> resolvedClass = requestedClass;
            if(Map.class.isAssignableFrom(requestedClass)) {
                resolvedClass = Map.class;
            }
            return (Mirror<T>) mirrorCache.get(resolvedClass);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
