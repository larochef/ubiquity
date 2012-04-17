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

    protected int objectToPrimitive(Integer value) {
        if(value != null) {
            return value.intValue();
        }
        return 0;
    }

    protected boolean objectToPrimitive(Boolean value) {
        if(value != null) {
            return value.booleanValue();
        }
        return false;
    }

    protected long objectToPrimitive(Long value) {
        if(value != null) {
            return value.longValue();
        }
        return 0l;
    }

    protected double objectToPrimitive(Double value) {
        if(value != null) {
            return value.doubleValue();
        }
        return 0d;
    }

    protected float objectToPrimitive(Float value) {
        if(value != null) {
            return value.floatValue();
        }
        return 0f;
    }

    protected char objectToPrimitive(Character value) {
        if(value != null) {
            return value.charValue();
        }
        return (char)0;
    }

    protected byte objectToPrimitive(Byte value) {
        if(value != null) {
            return value.byteValue();
        }
        return 0x0;
    }
}
