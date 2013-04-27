package org.ubiquity.bytecode;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Utility class to get / set properties from objects.
 * <br />
 * This utility class will not use reflexion, but instead, create classes able to parse the name and get / set the required property.
 *
 * Date: 16/06/12
 *
 * @author François LAROCHE
 */
public class PropertyUtils {

    private static final UbiquityClassLoader loader = new UbiquityClassLoader();
    private static final LoadingCache<Class<?>, ClassHandler<?>> cache = CacheBuilder.newBuilder()
            .build(new CacheLoader<Class<?>, ClassHandler<?>>() {
                @Override
                public ClassHandler<?> load(Class<?> copierKey) throws Exception {
                    return null;
                }
            });
}
