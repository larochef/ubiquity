package org.ubiquity.bytecode;

import org.ubiquity.Copier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author François LAROCHE
 */
public abstract class SimpleCopier <T, U> implements Copier<T, U>{
    @Override
    public List<U> map(List<T> elements) {
        List<U> result = new ArrayList<U>(elements.size());
        for(T element : elements) {
            result.add(map(element));
        }
        return result;
    }
}
