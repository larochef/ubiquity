package org.ubiquity;

import org.ubiquity.bytecode.CopyContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: 21/04/12
 *
 * @author François LAROCHE
 */
public class Ubiquity {
    private final CopyContext context;

    public Ubiquity() {
        this.context = new CopyContext();
    }

    public <T,U> U map(T source, Class<U> destiationClass) {
        Copier<T,U> copier = this.context.getCopier((Class<T>)source.getClass(), destiationClass);
        return copier.map(source);
    }

    public <T,U> void copy(T src, U destination) {
        Copier<T,U> copier = this.context.getCopier((Class<T>)src.getClass(), (Class<U>)destination.getClass());
        copier.copy(src, destination);
    }

    public <T,U> void registerCopier(Class<T> src, Class<U> target, Copier<T,U> copier) {
        this.context.registerCopier(src, target, copier);
    }

    public CollectionFactory getFactory() {
        return context.getFactory();
    }

    public void setFactory(CollectionFactory factory) {
        context.setFactory(factory);
    }
}
