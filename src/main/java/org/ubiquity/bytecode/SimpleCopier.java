package org.ubiquity.bytecode;

import org.ubiquity.Copier;

import java.util.ArrayList;
import java.util.List;

/**
 * Copier stub, making implementations easier.
 *
 * @param <T> The source class
 * @param <U> the destination class
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

    @Override
    public U[] map(T[] src, U[] target) {
        if(src == null) {
            return null;
        }
        U[] result = this.newArray(src.length);

        for(int i = 0; i < src.length; i++) {
            if(target != null && target.length > i) {
                this.copy(src[i], target[i]);
                result[i] = target[i];
            }
            else {
                result[i] = this.map(src[i]);
            }
        }
        return result;
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
     * Instanciate an object of the U class.
     * This method is needed, because instanciating U from reflexion is slow.
     * Implementors are expected to implement it with a "new".
     *
     * @return a newly instanciated object of class U
     */
    protected abstract U[] newArray(int capacity);

    /**
     * converts a java.lang.Short to a short.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Short to convert
     * @return the converted short
     */
    protected short convert(Short value) {
        if(value != null) {
            return value.shortValue();
        }
        return 0;
    }

    /**
     * converts a java.lang.Integer to an int.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Integer to convert
     * @return the converted int
     */
    protected int convert(Integer value) {
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
    protected boolean convert(Boolean value) {
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
    protected long convert(Long value) {
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
    protected double convert(Double value) {
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
    protected float convert(Float value) {
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
    protected char convert(Character value) {
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
    protected byte convert(Byte value) {
        if(value != null) {
            return value.byteValue();
        }
        return 0x0;
    }

    /**
     * converts a short to a  java.lang.Short
     *
     * @param value the short to convert
     * @return the converted java.lang.Short
     */
    protected Short convert(short value) {
        return Short.valueOf(value);
    }

    /**
     * converts an int to a  java.lang.Integer
     *
     * @param value the int to convert
     * @return the converted java.lang.Integer
     */
    protected Integer convert(int value) {
        return Integer.valueOf(value);
    }

    /**
     * converts a boolean to a java.lang.Boolean.
     *
     * @param value the boolean to convert
     * @return the converted java.lang.Boolean
     */
    protected Boolean convert(boolean value) {
        return Boolean.valueOf(value);
    }

    /**
     * converts a long to a java.lang.Long.
     *
     * @param value the long to convert
     * @return the converted java.lang.Long
     */
    protected Long convert(long value) {
        return Long.valueOf(value);
    }

    /**
     * converts a double to a java.lang.Double.
     *
     * @param value the double to convert
     * @return the converted java.lang.Double
     */
    protected Double convert(double value) {
        return Double.valueOf(value);
    }

    /**
     * converts a float to a java.lang.Float.
     *
     * @param value the float to convert
     * @return the converted java.lang.Float
     */
    protected Float convert(float value) {
        return Float.valueOf(value);
    }

    /**
     * converts a char to a java.lang.Character.
     *
     * @param value the char to convert
     * @return the converted java.lang.Character
     */
    protected Character convert(char value) {
        return Character.valueOf(value);
    }

    /**
     * converts a byte to a java.lang.Byte.
     *
     * @param value the byte to convert
     * @return the converted java.lang.Byte
     */
    protected Byte convert(byte value) {
        return Byte.valueOf(value);
    }

}
