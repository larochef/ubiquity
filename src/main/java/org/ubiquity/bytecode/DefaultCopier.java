package org.ubiquity.bytecode;

import org.ubiquity.Copier;
import org.ubiquity.util.CopierKey;

import java.util.List;

/**
 * Date: 06/06/12
 *
 * @author François LAROCHE
 */
final class DefaultCopier implements Copier<Object, Object> {

    private final CopyContext context;

    public DefaultCopier(CopyContext context) {
        this.context = context;
    }

    @Override
    public Object map(Object element) {
        if(element == null) {
            return null;
        }
        Copier<Object, Object> copier =  (Copier<Object, Object>)context.getCopier(
                CopierKey.newBuilder(element.getClass(), element.getClass()).build());
        return copier.map(element);
    }

    @Override
    public List<Object> map(List<Object> elements) {
        if(elements == null) {
            return null;
        }
        List<Object> result = context.getFactory().newList();
        for(Object src : elements) {
            result.add(map(src));
        }
        return result;
    }

    @Override
    public void copy(Object source, Object destination) {
        if(source == null || destination == null) {
            return ;
        }
        Copier<Object, Object> copier =  (Copier<Object, Object>)context.getCopier(
                CopierKey.newBuilder(source.getClass(), destination.getClass()).build());
        copier.copy(source, destination);
    }

    @Override
    public Object[] map(Object[] src, Object[] target) {
        // TODO : try to implement me nicely.
        if(src == null) {
            return null;
        }
        if(target == null) {
        }
        return new Object[0];
    }
}
