package org.ubiquity.bytecode;

import org.ubiquity.Copier;

import java.util.*;

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

    public List<U> map(List<T>source, List<U> destination) {
        List<U> result;
        if(destination != null) {
            result = destination;
        }
        else {
            result = this.context.getFactory().newList();
        }
        if(source.size() != result.size()) {
            result.clear();
        }
        if(result.isEmpty()) {
            for(T obj : source) {
                result.add(map(obj));
            }
        }
        else {
            Iterator<U> us = result.iterator();
            for(T obj : source) {
                copy(obj, us.next());
            }
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
     * Instanciate an array of U.
     * This method is needed, because instanciating U from reflexion is slow.
     * Implementors are expected to implement it with a "new".
     *
     * @return a newly instanciated array of U
     */
    protected abstract U[] newArray(int capacity);

    protected List<U> handleList(List<T> src, List<U> destination) {
        if(src == null) {
            return null;
        }
        List<U> result = destination;
        if(result == null) {
            result = this.context.getFactory().newList();
        }
        if(src.size() != result.size()) {
            result.clear();
            for(T elem : src) {
                result.add(map(elem));
            }
        }
        else {
            Iterator<U> us = result.iterator();
            for(T elem : src) {
                copy(elem, us.next());
            }
        }
        return result;
    }

    protected Set<U> handleSet(Set<T> src, Set<U> destination) {
        if(src == null) {
            return null;
        }
        Set<U> result = destination;
        if(result == null) {
            result = this.context.getFactory().newSet();
        }
        if(src.size() != result.size()) {
            result.clear();
            for(T elem : src) {
                result.add(map(elem));
            }
        }
        else {
            Iterator<U> us = result.iterator();
            for(T elem : src) {
                copy(elem, us.next());
            }
        }
        return result;
    }

    protected <K> Map<K, U> handleMap(Map<K,T> src, Map<K,U> destination) {
        if(src == null) {
            return null;
        }
        Map<K, U> result = destination;
        if(result == null) {
            result = this.context.getFactory().newMap();
        }
        for(K elem : src.keySet()) {
            U target = destination.get(elem);
            if(target == null) {
                target = newInstance();
                destination.put(elem, target);
            }
            copy(src.get(elem), target);
        }

        if(src.size() != destination.size()) {
            Set<K> destinationKeys = Collections.unmodifiableSet(destination.keySet());
            for(K key : destinationKeys) {
                if(!destination.containsKey(key)) {
                    destination.remove(key);
                }
            }
        }

        return result;
    }

    /**
     * converts a java.lang.Short to a short.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Short to convert
     * @return the converted short
     */
    protected final short convert(Short value) {
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
    protected final int convert(Integer value) {
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
    protected final boolean convert(Boolean value) {
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
    protected final long convert(Long value) {
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
    protected final double convert(Double value) {
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
    protected final float convert(Float value) {
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
    protected final char convert(Character value) {
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
    protected final byte convert(Byte value) {
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
    protected final Short convert(short value) {
        return Short.valueOf(value);
    }

    /**
     * converts an int to a  java.lang.Integer
     *
     * @param value the int to convert
     * @return the converted java.lang.Integer
     */
    protected final Integer convert(int value) {
        return Integer.valueOf(value);
    }

    /**
     * converts a boolean to a java.lang.Boolean.
     *
     * @param value the boolean to convert
     * @return the converted java.lang.Boolean
     */
    protected final Boolean convert(boolean value) {
        return Boolean.valueOf(value);
    }

    /**
     * converts a long to a java.lang.Long.
     *
     * @param value the long to convert
     * @return the converted java.lang.Long
     */
    protected final Long convert(long value) {
        return Long.valueOf(value);
    }

    /**
     * converts a double to a java.lang.Double.
     *
     * @param value the double to convert
     * @return the converted java.lang.Double
     */
    protected final Double convert(double value) {
        return Double.valueOf(value);
    }

    /**
     * converts a float to a java.lang.Float.
     *
     * @param value the float to convert
     * @return the converted java.lang.Float
     */
    protected final Float convert(float value) {
        return Float.valueOf(value);
    }

    /**
     * converts a char to a java.lang.Character.
     *
     * @param value the char to convert
     * @return the converted java.lang.Character
     */
    protected final Character convert(char value) {
        return Character.valueOf(value);
    }

    /**
     * converts a byte to a java.lang.Byte.
     *
     * @param value the byte to convert
     * @return the converted java.lang.Byte
     */
    protected final Byte convert(byte value) {
        return Byte.valueOf(value);
    }

    /**
     * converts a java.lang.Short to a short.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Short to convert
     * @return the converted short
     */
    protected final short[] convert(Short[] value) {
        if(value == null) {
            return null;
        }
        short [] result = new short[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a java.lang.Integer to an int.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Integer to convert
     * @return the converted int
     */
    protected final int[] convert(Integer[] value) {
        if(value == null) {
            return null;
        }
        int[] result = new int[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a java.lang.Boolean to a boolean.
     * Of the given parameter is null, this method will return false.
     *
     * @param value the java.lang.Boolean to convert
     * @return the converted boolean
     */
    protected final boolean[] convert(Boolean[] value) {
        if(value == null) {
            return null;
        }
        boolean[] result = new boolean[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a java.lang.Long to a long.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Long to convert
     * @return the converted long
     */
    protected final long[] convert(Long[] value) {
        if(value == null) {
            return null;
        }
        long[] result = new long[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a java.lang.Double to a double.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Double to convert
     * @return the converted double
     */
    protected final double[] convert(Double[] value) {
        if(value == null) {
            return null;
        }
        double[] result = new double[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a java.lang.Float to a float.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Float to convert
     * @return the converted float
     */
    protected final float[] convert(Float[] value) {
        if(value == null) {
            return null;
        }
        float[] result = new float[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a java.lang.Character to a char.
     * Of the given parameter is null, this method will return the char wielding the 0 value.
     *
     * @param value the java.lang.Character to convert
     * @return the converted char
     */
    protected final char[] convert(Character[] value) {
        if(value == null) {
            return null;
        }
        char[] result = new char[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a java.lang.Byte to a byte.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Bte to convert
     * @return the converted byte
     */
    protected final byte[] convert(Byte[] value) {
        if(value == null) {
            return null;
        }
        byte[] result = new byte[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a short to a  java.lang.Short
     *
     * @param value the short to convert
     * @return the converted java.lang.Short
     */
    protected final Short[] convert(short[] value) {
        if(value == null) {
            return null;
        }
        Short[] result = new Short[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts an int to a  java.lang.Integer
     *
     * @param value the int to convert
     * @return the converted java.lang.Integer
     */
    protected final Integer[] convert(int[] value) {
        if(value == null) {
            return null;
        }
        Integer[] result = new Integer[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a boolean to a java.lang.Boolean.
     *
     * @param value the boolean to convert
     * @return the converted java.lang.Boolean
     */
    protected final Boolean[] convert(boolean[] value) {
        if(value == null) {
            return null;
        }
        Boolean[] result = new Boolean[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a long to a java.lang.Long.
     *
     * @param value the long to convert
     * @return the converted java.lang.Long
     */
    protected final Long[] convert(long[] value) {
        if(value == null) {
            return null;
        }
        Long[] result = new Long[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a double to a java.lang.Double.
     *
     * @param value the double to convert
     * @return the converted java.lang.Double
     */
    protected final Double[] convert(double[] value) {
        if(value == null) {
            return null;
        }
        Double[] result = new Double[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a float to a java.lang.Float.
     *
     * @param value the float to convert
     * @return the converted java.lang.Float
     */
    protected final Float[] convert(float[] value) {
        if(value == null) {
            return null;
        }
        Float[] result = new Float[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a char to a java.lang.Character.
     *
     * @param value the char to convert
     * @return the converted java.lang.Character
     */
    protected final Character[] convert(char[] value) {
        if(value == null) {
            return null;
        }
        Character[] result = new Character[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

    /**
     * converts a byte to a java.lang.Byte.
     *
     * @param value the byte to convert
     * @return the converted java.lang.Byte
     */
    protected final Byte[] convert(byte[] value) {
        if(value == null) {
            return null;
        }
        Byte[] result = new Byte[value.length];
        for(int i = 0; i < value.length; i++) {
            result[i] = convert(value[i]);
        }
        return result;
    }

}
