package org.ubiquity.bytecode;

import org.ubiquity.CollectionFactory;
import org.ubiquity.Copier;
import org.ubiquity.logging.LoggerFactory;
import org.ubiquity.logging.impl.JdkLogging;
import org.ubiquity.util.CopierKey;
import org.ubiquity.util.DefaultCollectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private Map<CopierKey<?,?>, Copier<?,?>> copiers;
    private final List<CopierKey<?,?>> requiredTuples;
    private final CopierGenerator generator;
    private CollectionFactory factory;
    private LoggerFactory loggerFactory;

    public CopyContext() {
        this.copiers = new ConcurrentHashMap<CopierKey<?,?>, Copier<?, ?>>();
        this.requiredTuples = new ArrayList<CopierKey<?,?>>();
        this.generator = new CopierGenerator();
        this.factory = new DefaultCollectionFactory();
        this.loggerFactory = JdkLogging.getJdkLoggerFactory();

        this.registerCopier(CopierKey.newBuilder(Object.class, Object.class).build(), new DefaultCopier(this));
    }

    @SuppressWarnings("Unchecked")
    public final synchronized <T, U> Copier<T,U> getCopier(CopierKey<T, U> key) {
        if(!copiers.containsKey(key)) {
            this.requireCopier(key);
            try {
                this.createRequiredCopiers();
            } catch (Exception e) {
                loggerFactory.getLogger(getClass()).error("Unable to create copier : ", e);
            }
        }
        Copier<T,U> result = (Copier<T,U>) copiers.get(key);
        if(result == null) {
            throw new IllegalStateException("Unable to find the builder, it was supposed to be built.");
        }
        return result;
    }

    public final <T, U> void registerCopier(CopierKey<T,U> key, Copier<T,U> copier) {
        this.copiers.put(key, copier);
    }

    final void requireCopier(CopierKey<?,?> key) {
        // If copier already exists, nothing to do.
        if(copiers.containsKey(key)) {
            return;
        }
        // Require copier so that the conversion can be done.
        if(!requiredTuples.contains(key)) {
            synchronized (this.requiredTuples) {
                this.requiredTuples.add(key);
            }
        }
    }

    @SuppressWarnings("Unchecked")
    final <T, U> void createRequiredCopiers() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        while(!this.requiredTuples.isEmpty()) {
            synchronized (this.requiredTuples) {
                CopierKey<T,U> key = (CopierKey<T,U>)this.requiredTuples.remove(0);
                registerCopier(key, this.generator.createCopier(key, this));
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
