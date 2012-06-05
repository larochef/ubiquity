package org.ubiquity;

import org.ubiquity.bytecode.CopyContext;
import org.ubiquity.util.CopierKey;

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

    public <T,U> U map(T source, Class<U> destinationClass) {
        Copier<T,U> copier = this.context.getCopier(CopierKey.newBuilder(source.getClass(), destinationClass).build());
        return copier.map(source);
    }

    public <T,U> void copy(T src, U destination) {
        Copier<T,U> copier = this.context.getCopier(CopierKey.newBuilder(src.getClass(), destination.getClass()).build());
        copier.copy(src, destination);
    }

    public <T,U> void registerCopier(CopierKey<T,U> key, Copier<T,U> copier) {
        this.context.registerCopier(key, copier);
    }

    public CollectionFactory getFactory() {
        return context.getFactory();
    }

    public void setFactory(CollectionFactory factory) {
        context.setFactory(factory);
    }
}
