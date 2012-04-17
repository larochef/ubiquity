package org.ubiquity.bytecode;

import org.ubiquity.Copier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author François LAROCHE
 */
public abstract class SimpleCopier <T, U> implements Copier<T, U>{

    protected final CopyContext context;

    protected SimpleCopier(CopyContext context) {
        this.context = context;
    }

    @Override
    public List<U> map(List<T> elements) {
        List<U> result = new ArrayList<U>(elements.size());
        for(T element : elements) {
            result.add(map(element));
        }
        return result;
    }

    @Override
    public U map(T element) {
        U target = newInstance();
        copy(element, target);
        return target;
    }

    protected abstract U newInstance();
}
