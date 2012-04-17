package org.ubiquity.bytecode;

import org.ubiquity.Copier;

import java.util.ArrayList;
import java.util.List;

/**
 * Copier stub, making implementations easier.
 *
 * @param <T></T> The source class
 * @param <U></U> the destination class
 *
 * @author François LAROCHE
 */
public abstract class SimpleCopier <T, U> implements Copier<T, U>{

    protected final CopyContext context;

    /**
     * Create a new {@link SimpleCopier} setting the {@link CopyContext} to be used for conversions
     *
     * @param context the {@link CopyContext} to be used to copy complex objects
     */
    protected SimpleCopier(CopyContext context) {
        this.context = context;
    }

    /**
     * @inheritdoc
     */
    @Override
    public List<U> map(List<T> elements) {
        List<U> result = new ArrayList<U>(elements.size());
        for(T element : elements) {
            result.add(map(element));
        }
        return result;
    }

    /**
     * @inheritdoc
     */
    @Override
    public U map(T element) {
        U target = newInstance();
        copy(element, target);
        return target;
    }

    /**
     * Instanciate an object of the U class.
     * This method is needed, because instanciating U from reflexion is slow.
     * Implementors are expected to implement it with a "new".
     *
     * @return a newly instanciated object of class U
     */
    protected abstract U newInstance();

    /**
     * converts a java.lang.Integer to an int.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Integer to convert
     * @return the converted int
     */
    protected int objectToPrimitive(Integer value) {
        if(value != null) {
            return value.intValue();
        }
        return 0;
    }

    /**
     * converts a java.lang.Boolean to a boolean.
     * Of the given parameter is null, this method will return false.
     *
     * @param value the java.lang.Boolean to convert
     * @return the converted boolean
     */
    protected boolean objectToPrimitive(Boolean value) {
        if(value != null) {
            return value.booleanValue();
        }
        return false;
    }

    /**
     * converts a java.lang.Long to a long.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Long to convert
     * @return the converted long
     */
    protected long objectToPrimitive(Long value) {
        if(value != null) {
            return value.longValue();
        }
        return 0l;
    }

    /**
     * converts a java.lang.Double to a double.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Double to convert
     * @return the converted double
     */
    protected double objectToPrimitive(Double value) {
        if(value != null) {
            return value.doubleValue();
        }
        return 0d;
    }

    /**
     * converts a java.lang.Float to a float.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Float to convert
     * @return the converted float
     */
    protected float objectToPrimitive(Float value) {
        if(value != null) {
            return value.floatValue();
        }
        return 0f;
    }

    /**
     * converts a java.lang.Character to a char.
     * Of the given parameter is null, this method will return the char wielding the 0 value.
     *
     * @param value the java.lang.Character to convert
     * @return the converted char
     */
    protected char objectToPrimitive(Character value) {
        if(value != null) {
            return value.charValue();
        }
        return (char)0;
    }

    /**
     * converts a java.lang.Byte to a byte.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Bte to convert
     * @return the converted byte
     */
    protected byte objectToPrimitive(Byte value) {
        if(value != null) {
            return value.byteValue();
        }
        return 0x0;
    }
}
