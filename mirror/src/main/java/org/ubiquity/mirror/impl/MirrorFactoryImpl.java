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

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
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

    private final LoadingCache<MirrorKey, Mirror<?>> mirrorCache;

    final SimpleClassLoader loader;

    public MirrorFactoryImpl(final SimpleClassLoader loader) {
        this.loader = loader;
        this.mirrorCache = CacheBuilder.newBuilder().build(new CacheLoader<MirrorKey, Mirror<?>>() {
            @Override
            public Mirror<?> load(MirrorKey aKey) throws Exception {
                Collection<ClassDefinition> definitions =
                        MirrorGenerator.generateMirror(aKey.mirrorHandledClass, aKey.generics);
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
        this.mirrorCache.put(new MirrorKey(Map.class, null), new MapMirror());
    }

    @Override
    public <T> Mirror<T> getMirror(Class<T> requestedClass) {
         return getMirror(requestedClass, null);
    }

    @Override
    public <T> Mirror<T> getMirror(Class<T> requestedClass, Map<String, Class<?>> generics) {
        try {
            Class<?> resolvedClass = requestedClass;
            if(Map.class.isAssignableFrom(requestedClass)) {
                resolvedClass = Map.class;
            }
            return (Mirror<T>) mirrorCache.get(new MirrorKey(resolvedClass, generics));
        } catch (ExecutionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static final class MirrorKey {
        final Class<?> mirrorHandledClass;
        final Map<String, Class<?>> generics;

        MirrorKey(Class<?> mirrorHandledClass, Map<String, Class<?>> generics) {
            this.generics = generics == null ? ImmutableMap.<String, Class<?>>of() : ImmutableMap.copyOf(generics);
            this.mirrorHandledClass = mirrorHandledClass;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || obj.getClass() != getClass()) {
                return false;
            }
            MirrorKey other = (MirrorKey) obj;
            return this.mirrorHandledClass == other.mirrorHandledClass
                    && this.generics.equals(other.generics);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.mirrorHandledClass, this.generics);
        }
    }
}
