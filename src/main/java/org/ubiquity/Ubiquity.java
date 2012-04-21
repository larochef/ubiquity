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

    public Class<? extends List> getDefaultListImplementation() {
        return context.getDefaultListImplementation();
    }

    public void setDefaultListImplementation(Class<? extends List> defaultListImplementation) {
        context.setDefaultListImplementation(defaultListImplementation);
    }

    public Class<? extends Set> getDefaultSetImplementation() {
        return context.getDefaultSetImplementation();
    }

    public void setDefaultSetImplementation(Class<? extends Set> defaultSetImplementation) {
        context.setDefaultSetImplementation(defaultSetImplementation);
    }

    public Class<? extends Map> getDefaultMapImplementation() {
        return context.getDefaultMapImplementation();
    }

    public void setDefaultMapImplementation(Class<? extends Map> defaultMapImplementation) {
        context.setDefaultMapImplementation(defaultMapImplementation);
    }
}
