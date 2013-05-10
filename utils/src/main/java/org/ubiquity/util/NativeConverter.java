/**
 * Copyright 2012 ubiquity-copy

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.ubiquity.util;

/**
 * Utility class used to box native &lt;-&gt; primitive
 *
 */
public final class NativeConverter {

    private NativeConverter() {
        // I am a utility class
    }

    /**
     * converts a java.lang.Short to a short.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Short to convert
     * @return the converted short
     */
    public final short convert(Short value) {
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
    public final int convert(Integer value) {
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
    public final boolean convert(Boolean value) {
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
    public final long convert(Long value) {
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
    public final double convert(Double value) {
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
    public final float convert(Float value) {
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
    public final char convert(Character value) {
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
    public final byte convert(Byte value) {
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
    public final Short convert(short value) {
        return Short.valueOf(value);
    }

    /**
     * converts an int to a  java.lang.Integer
     *
     * @param value the int to convert
     * @return the converted java.lang.Integer
     */
    public final Integer convert(int value) {
        return Integer.valueOf(value);
    }

    /**
     * converts a boolean to a java.lang.Boolean.
     *
     * @param value the boolean to convert
     * @return the converted java.lang.Boolean
     */
    public final Boolean convert(boolean value) {
        return Boolean.valueOf(value);
    }

    /**
     * converts a long to a java.lang.Long.
     *
     * @param value the long to convert
     * @return the converted java.lang.Long
     */
    public final Long convert(long value) {
        return Long.valueOf(value);
    }

    /**
     * converts a double to a java.lang.Double.
     *
     * @param value the double to convert
     * @return the converted java.lang.Double
     */
    public final Double convert(double value) {
        return Double.valueOf(value);
    }

    /**
     * converts a float to a java.lang.Float.
     *
     * @param value the float to convert
     * @return the converted java.lang.Float
     */
    public final Float convert(float value) {
        return Float.valueOf(value);
    }

    /**
     * converts a char to a java.lang.Character.
     *
     * @param value the char to convert
     * @return the converted java.lang.Character
     */
    public final Character convert(char value) {
        return Character.valueOf(value);
    }

    /**
     * converts a byte to a java.lang.Byte.
     *
     * @param value the byte to convert
     * @return the converted java.lang.Byte
     */
    public final Byte convert(byte value) {
        return Byte.valueOf(value);
    }

    /**
     * converts a java.lang.Short to a short.
     * Of the given parameter is null, this method will return 0.
     *
     * @param value the java.lang.Short to convert
     * @return the converted short
     */
    public final short[] convert(Short[] value) {
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
    public final int[] convert(Integer[] value) {
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
    public final boolean[] convert(Boolean[] value) {
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
    public final long[] convert(Long[] value) {
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
    public final double[] convert(Double[] value) {
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
    public final float[] convert(Float[] value) {
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
    public final char[] convert(Character[] value) {
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
    public final byte[] convert(Byte[] value) {
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
    public final Short[] convert(short[] value) {
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
    public final Integer[] convert(int[] value) {
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
    public final Boolean[] convert(boolean[] value) {
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
    public final Long[] convert(long[] value) {
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
    public final Double[] convert(double[] value) {
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
    public final Float[] convert(float[] value) {
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
    public final Character[] convert(char[] value) {
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
    public final Byte[] convert(byte[] value) {
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
