package org.ubiquity.bytecode;

import org.ubiquity.Ubiquity;

/**
 * Date: 01/06/12
 *
 * @author François LAROCHE
 */
public class TestBoxedArrays {
    private static final Ubiquity ubiquity = new Ubiquity();

    public static class PrimitiveArrayClass {
        private int [] ints;
        private boolean [] bools;
        private long [] longs;
        private double [] doubles;
        private float [] floats;
        private byte [] bytes;
        private char [] chars;
        private short [] shorts;

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
    }

    public static class BoxedArrayClass {
        private Integer [] ints;
        private Boolean [] bools;
        private Long [] longs;
        private Double [] doubles;
        private Float [] floats;
        private Byte [] bytes;
        private Character [] chars;
        private Short [] shorts;

        public Integer[] getInts() {
            return ints;
        }

        public void setInts(Integer[] ints) {
            this.ints = ints;
        }

        public Boolean[] getBools() {
            return bools;
        }

        public void setBools(Boolean[] bools) {
            this.bools = bools;
        }

        public Long[] getLongs() {
            return longs;
        }

        public void setLongs(Long[] longs) {
            this.longs = longs;
        }

        public Double[] getDoubles() {
            return doubles;
        }

        public void setDoubles(Double[] doubles) {
            this.doubles = doubles;
        }

        public Float[] getFloats() {
            return floats;
        }

        public void setFloats(Float[] floats) {
            this.floats = floats;
        }

        public Byte[] getBytes() {
            return bytes;
        }

        public void setBytes(Byte[] bytes) {
            this.bytes = bytes;
        }

        public Character[] getChars() {
            return chars;
        }

        public void setChars(Character[] chars) {
            this.chars = chars;
        }

        public Short[] getShorts() {
            return shorts;
        }

        public void setShorts(Short[] shorts) {
            this.shorts = shorts;
        }
    }
}
