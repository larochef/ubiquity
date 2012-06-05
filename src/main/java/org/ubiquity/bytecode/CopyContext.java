package org.ubiquity.bytecode;

import org.ubiquity.CollectionFactory;
import org.ubiquity.Copier;
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
public class CopyContext {

    private Map<CopierKey<?,?>, Copier<?,?>> copiers;
    private final List<CopierKey<?,?>> requiredTuples;
    private final CopierGenerator generator;
    private CollectionFactory factory;

    public CopyContext() {
        this.copiers = new ConcurrentHashMap<CopierKey<?,?>, Copier<?, ?>>();
        this.requiredTuples = new ArrayList<CopierKey<?,?>>();
        this.generator = new CopierGenerator();
        this.factory = new DefaultCollectionFactory();
    }

    public synchronized <T, U> Copier<T,U> getCopier(CopierKey key) {
        if(!copiers.containsKey(key)) {
            this.requireCopier(key);
            try {
                this.createRequiredCopiers();
            } catch (Exception e) {
                // TODO : handle the exception !!
                e.printStackTrace();
            }
        }
        @SuppressWarnings("Unchecked")
        Copier<T,U> result = (Copier<T,U>) copiers.get(key);
        if(result == null) {
            throw new IllegalStateException("Unable to find the builder, it was supposed to be built.");
        }
        return result;
    }

    public <T, U> void registerCopier(CopierKey<T,U> key, Copier<T,U> copier) {
        this.copiers.put(key, copier);
    }

    void requireCopier(CopierKey<?,?> key) {
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
    <T, U> void createRequiredCopiers() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        while(!this.requiredTuples.isEmpty()) {
            synchronized (this.requiredTuples) {
                CopierKey<T,U> key = (CopierKey<T,U>)this.requiredTuples.remove(0);
                registerCopier(key, this.generator.createCopier(key, this));
            }
        }
    }

    public CollectionFactory getFactory() {
        return factory;
    }

    public void setFactory(CollectionFactory factory) {
        this.factory = factory;
    }
}
