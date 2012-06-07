package org.ubiquity.bytecode;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.ubiquity.CollectionFactory;
import org.ubiquity.Copier;
import org.ubiquity.logging.Logger;
import org.ubiquity.logging.impl.JdkLogging;
import org.ubiquity.util.CopierKey;
import org.ubiquity.util.DefaultCollectionFactory;

import java.util.concurrent.ExecutionException;

/**
 * Class storing the different copiers, making all copiers available to other copiers.
 * <br />
 *
 * Date: 17/04/12
 * Time: 09:33
 *
 * @author françois LAROCHE
 */
public final class CopyContext {

    private static final CopierGenerator generator = new CopierGenerator();

    private final LoadingCache<CopierKey<?,?>, Copier<?,?>> cache;
    private final Logger logger;
    private CollectionFactory factory;

    public CopyContext() {
        this.cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<CopierKey<?,?>,Copier<?, ?>>(){
                    @Override
                    public Copier<?, ?> load(CopierKey<?, ?> copierKey) throws Exception {
                        return generator.createCopier(copierKey, CopyContext.this);
                    }
                });
        this.factory = new DefaultCollectionFactory();
        this.logger = JdkLogging.getJdkLoggerFactory().getLogger(CopyContext.class);
        this.registerCopier(CopierKey.newKey(Object.class, Object.class), new DefaultCopier(this));
    }

    public final <T, U> Copier<T,U> getCopier(final CopierKey<T, U> key) {
        try {
            @SuppressWarnings("unchecked") Copier<T, U> copier = (Copier<T,U>) cache.get(key);
            return copier;
        } catch (ExecutionException e) {
            logger.error("Unable to create copier : ", e);
        }
        return getCopier(key);
    }

    public final <T, U> void registerCopier(CopierKey<T,U> key, Copier<T,U> copier) {
        this.cache.put(key, copier);
    }

    public final CollectionFactory getFactory() {
        return factory;
    }

    public final void setFactory(CollectionFactory factory) {
        this.factory = factory;
    }
}
