package org.ubiquity.bytecode;

import org.ubiquity.CollectionFactory;
import org.ubiquity.Copier;
import org.ubiquity.logging.Logger;
import org.ubiquity.logging.impl.JdkLogging;
import org.ubiquity.util.CopierKey;
import org.ubiquity.util.DefaultCollectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<CopierKey<?,?>, Copier<?,?>> copiers;
    private final Queue<CopierKey<?,?>> requiredTuples;
    private final CopierGenerator generator;
    private CollectionFactory factory;
    private final Logger logger;

    public CopyContext() {
        this.copiers = new ConcurrentHashMap<CopierKey<?,?>, Copier<?, ?>>();
        this.requiredTuples = new ArrayDeque<CopierKey<?, ?>>();
        this.generator = new CopierGenerator();
        this.factory = new DefaultCollectionFactory();
        this.logger = JdkLogging.getJdkLoggerFactory().getLogger(CopyContext.class);

        this.registerCopier(CopierKey.newBuilder(Object.class, Object.class).build(), new DefaultCopier(this));
    }

    public final <T, U> Copier<T,U> getCopier(CopierKey<T, U> key) {
        @SuppressWarnings("unchecked") Copier<T, U> result = (Copier<T, U>) copiers.get(key);
        if(result == null) {
            result = this.createCopier(key);
            if(result == null) {
                throw new IllegalStateException("Unable to find the builder, it was supposed to be built.");
            }
        }
        return result;
    }


    private <T, U> Copier<T,U> createCopier(CopierKey<T, U> key) {
        // Group the 2 calls in a single synchronized block, to avoid 2 consecutive locks.
        synchronized (this.requiredTuples) {
            this.requireCopier(key);
            try {
                this.createRequiredCopiers();
                @SuppressWarnings("unchecked") Copier<T, U> copier = (Copier<T, U>) copiers.get(key);
                return copier;
            } catch (InstantiationException e) {
                logger.error("Unable to create copier : ", e);
            } catch (IllegalAccessException e) {
                logger.error("Unable to create copier : ", e);
            } catch (InvocationTargetException e) {
                logger.error("Unable to create copier : ", e);
            } catch (NoSuchMethodException e) {
                logger.error("Unable to create copier : ", e);
            }
        }
        return null;
    }


    public final <T, U> void registerCopier(CopierKey<T,U> key, Copier<T,U> copier) {
        this.copiers.put(key, copier);
    }

    final void requireCopier(CopierKey<?,?> key) {
        // Require copier so that the conversion can be done.
        synchronized (this.requiredTuples) {
            this.requiredTuples.add(key);
        }
    }

    final <T, U> void createRequiredCopiers() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        synchronized (this.requiredTuples) {
            while(!this.requiredTuples.isEmpty()) {
                @SuppressWarnings("unchecked") CopierKey<T, U> key = (CopierKey<T, U>) this.requiredTuples.remove();
                // If copier already exists, nothing to do.
                if(!copiers.containsKey(key)) {
                    registerCopier(key, this.generator.createCopier(key, this));
                }
            }
        }
    }

    public final CollectionFactory getFactory() {
        return factory;
    }

    public final void setFactory(CollectionFactory factory) {
        this.factory = factory;
    }
}
