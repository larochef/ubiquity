package org.ubiquity.bytecode;

import org.ubiquity.CollectionFactory;
import org.ubiquity.Copier;
import org.ubiquity.util.DefaultCollectionFactory;
import org.ubiquity.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO : explain.me
 * Date: 17/04/12
 * Time: 09:33
 *
 * @author françois LAROCHE
 */
public class CopyContext {

    private Map<Tuple<?,?>, Copier<?,?>> copiers;
    private final Map<Tuple<Class<?>,Class<?>>, Tuple<Map<String, String>, Map<String, String>>> requiredTuples;
    private final CopierGenerator generator;
    private CollectionFactory factory;

    public CopyContext() {
        this.copiers = new ConcurrentHashMap<Tuple<?, ?>, Copier<?, ?>>();
        this.requiredTuples = new HashMap<Tuple<Class<?>, Class<?>>, Tuple<Map<String, String>, Map<String, String>>>();
        this.generator = new CopierGenerator();
        this.factory = new DefaultCollectionFactory();
    }

    public synchronized <T, U> Copier<T,U> getCopier(Class<T> source, Class<U> destination, Map<String, String> srcGenerics, Map<String, String> destinationGenerics) {
        Tuple<Class<T>,Class<U>> key = new Tuple<Class<T>, Class<U>>(source, destination);
        if(!copiers.containsKey(key)) {
            this.requireCopier(source, destination, srcGenerics, destinationGenerics);
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

    public synchronized <T, U> Copier<T,U> getCopier(Class<T> source, Class<U> destination) {
        return getCopier(source, destination, null, null);
    }

    public <T, U> void registerCopier(Class<T> source, Class<U> destination, Copier<T,U> copier) {
        this.copiers.put(new Tuple<Class<T>, Class<U>>(source, destination), copier);
    }

    <T, U> void requireCopier(Class<T> source, Class<U> destination, Map<String, String> srcGenerics, Map<String, String> destinationGenerics) {
        Tuple<Class<?>, Class<?>> key = new Tuple<Class<?>, Class<?>>(source, destination);
        // If copier already exists, nothing to do.
        if(copiers.containsKey(key)) {
            return;
        }
        // Require copier so that the conversion can be done.
        if(!requiredTuples.containsKey(key)) {
            synchronized (this.requiredTuples) {
                this.requiredTuples.put(key, new Tuple<Map<String, String>, Map<String, String>>(srcGenerics, destinationGenerics));
            }
        }
    }

    @SuppressWarnings("Unchecked")
    <T, U> void createRequiredCopiers() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        while(!this.requiredTuples.isEmpty()) {
            synchronized (this.requiredTuples) {
                Tuple<Class<?>, Class<?>> tuple = this.requiredTuples.keySet().iterator().next();
                Tuple<Map<String, String>, Map<String, String>> generics = this.requiredTuples.remove(tuple);
                Class<T> source = (Class<T>) tuple.tObject;
                Class<U> destination = (Class<U>) tuple.uObject;

                registerCopier(source, destination,
                        this.generator.createCopier(source, destination, this, generics.tObject, generics.uObject));
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
