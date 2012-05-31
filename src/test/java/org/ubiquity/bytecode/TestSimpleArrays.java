package org.ubiquity.bytecode;

import org.ubiquity.Ubiquity;

/**
 * Date: 01/06/12
 *
 * @author François LAROCHE
 */
public class TestSimpleArrays {
    private static final Ubiquity ubiquity = new Ubiquity();

    // Class containing all the possible arrays
    public class ArraysTest {
        private int [] ints;
        private boolean [] bools;
        private long [] longs;
        private double [] doubles;
        private float [] floats;
        private byte [] bytes;
        private char [] chars;
        private short [] shorts;

        private Integer [] intObjects;
        private Boolean [] boolObjects;
        private Long [] longObjects;
        private Double [] doubleObjects;
        private Float [] floatObjects;
        private Byte [] byteObjects;
        private Character [] charObjects;
        private Short [] shortObjects;

        public int[] getInts() {
            return ints;
        }

        public void setInts(int[] ints) {
            this.ints = ints;
        }

        public boolean[] getBools() {
            return bools;
        }

        public void setBools(boolean[] bools) {
            this.bools = bools;
        }

        public long[] getLongs() {
            return longs;
        }

        public void setLongs(long[] longs) {
            this.longs = longs;
        }

        public double[] getDoubles() {
            return doubles;
        }

        public void setDoubles(double[] doubles) {
            this.doubles = doubles;
        }

        public float[] getFloats() {
            return floats;
        }

        public void setFloats(float[] floats) {
            this.floats = floats;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public char[] getChars() {
            return chars;
        }

        public void setChars(char[] chars) {
            this.chars = chars;
        }

        public short[] getShorts() {
            return shorts;
        }

        public void setShorts(short[] shorts) {
            this.shorts = shorts;
        }

        public Integer[] getIntObjects() {
            return intObjects;
        }

        public void setIntObjects(Integer[] intObjects) {
            this.intObjects = intObjects;
        }

        public Boolean[] getBoolObjects() {
            return boolObjects;
        }

        public void setBoolObjects(Boolean[] boolObjects) {
            this.boolObjects = boolObjects;
        }

        public Long[] getLongObjects() {
            return longObjects;
        }

        public void setLongObjects(Long[] longObjects) {
            this.longObjects = longObjects;
        }

        public Double[] getDoubleObjects() {
            return doubleObjects;
        }

        public void setDoubleObjects(Double[] doubleObjects) {
            this.doubleObjects = doubleObjects;
        }

        public Float[] getFloatObjects() {
            return floatObjects;
        }

        public void setFloatObjects(Float[] floatObjects) {
            this.floatObjects = floatObjects;
        }

        public Byte[] getByteObjects() {
            return byteObjects;
        }

        public void setByteObjects(Byte[] byteObjects) {
            this.byteObjects = byteObjects;
        }

        public Character[] getCharObjects() {
            return charObjects;
        }

        public void setCharObjects(Character[] charObjects) {
            this.charObjects = charObjects;
        }

        public Short[] getShortObjects() {
            return shortObjects;
        }

        public void setShortObjects(Short[] shortObjects) {
            this.shortObjects = shortObjects;
        }
    }
}
