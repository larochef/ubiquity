package org.ubiquity.bytecode;

import org.ubiquity.Copier;
import org.ubiquity.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO : explain.me
 * Date: 17/04/12
 * Time: 09:33
 *
 * @Author françois LAROCHE
 */
public class CopyContext {

    private Map<Tuple<?,?>, Copier<?,?>> copiers;
    private final List<Tuple<?,?>> requiredTuples;
    private final CopierGenerator generator;

    public CopyContext() {
        this.copiers = new ConcurrentHashMap<Tuple<?, ?>, Copier<?, ?>>();
        this.requiredTuples = new ArrayList<Tuple<?, ?>>();
        this.generator = new CopierGenerator();
    }

    public <T, U> Copier<T,U> getCopier(Class<T> source, Class<U> destination) {
        Tuple<Class<T>,Class<U>> key = new Tuple<Class<T>, Class<U>>(source, destination);
        if(!copiers.containsKey(key)) {
            this.requireCopier(source, destination);
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

    public <T, U> void registerCopier(Class<T> source, Class<U> destination, Copier<T,U> copier) {
        this.copiers.put(new Tuple<Class<T>, Class<U>>(source, destination), copier);
    }

    <T, U> void requireCopier(Class<T> source, Class<U> destination) {
        Tuple<Class<T>, Class<U>> key = new Tuple<Class<T>, Class<U>>(source, destination);
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
            Tuple<Class<T>, Class<U>> tuple;
            synchronized (this.requiredTuples) {
                tuple = (Tuple<Class<T>, Class<U>>)this.requiredTuples.remove(0);
            }
            Class<T> source = tuple.tObject;
            Class<U> destination = tuple.uObject;
            registerCopier(source, destination, this.generator.createCopier(source, destination, this));
        }
    }
}
