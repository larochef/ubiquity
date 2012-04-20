package org.ubiquity.bytecode;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.ubiquity.Copier;
import org.ubiquity.util.ClassTuple;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO : explain.me
 * Date: 17/04/12
 * Time: 09:33
 *
 * @Author françois LAROCHE
 */
public class CopyContext {

    private Map<ClassTuple<?,?>, Copier<?,?>> copiers;
    private List<ClassTuple<?,?>> requiredTuples;

    public CopyContext() {
        this.copiers = new HashMap<ClassTuple<?, ?>, Copier<?, ?>>();
        this.requiredTuples = new ArrayList<ClassTuple<?, ?>>();
    }

    public <T, U> Copier<T,U> getCopier(Class<T> source, Class<U> destination) {
        ClassTuple<T,U> key = new ClassTuple<T, U>(source, destination);
        if(!copiers.containsKey(key)) {
            this.requireCopier(source, destination);
            try {
                this.createRequiredCopiers();
            } catch (Exception e) {
                // TODO : handle the exception !!
                e.printStackTrace();
            }
        }
        return (Copier<T,U>) copiers.get(key);
    }

    public <T, U> void registerCopier(Class<T> source, Class<U> destination, Copier<T,U> copier) {
        this.copiers.put(new ClassTuple<T, U>(source, destination), copier);
    }

    <T, U> void requireCopier(Class<T> source, Class<U> destination) {
        ClassTuple<T, U> key = new ClassTuple<T, U>(source, destination);
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

    <T, U> void createRequiredCopiers() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        while(!this.requiredTuples.isEmpty()) {
            ClassTuple<T,U> tuple;
            synchronized (this.requiredTuples) {
                tuple = (ClassTuple<T,U>)this.requiredTuples.remove(0);
            }
            Class<T> source = tuple.gettClass();
            Class<U> destination = tuple.getuClass();
            registerCopier(source, destination, CopierGenerator.createCopier(source, destination, this));
        }
    }
}
