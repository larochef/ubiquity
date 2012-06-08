package org.ubiquity.bytecode;

import org.junit.Test;
import org.ubiquity.Ubiquity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

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

    @Test
    public void testNullArrays() {
        PrimitiveArrayClass primitive = new PrimitiveArrayClass();
        BoxedArrayClass boxed = ubiquity.map(primitive, BoxedArrayClass.class);

        assertNull(boxed.getBools());
        assertNull(boxed.getBytes());
        assertNull(boxed.getChars());
        assertNull(boxed.getDoubles());
        assertNull(boxed.getFloats());
        assertNull(boxed.getInts());
        assertNull(boxed.getLongs());
        assertNull(boxed.getShorts());

        boxed = new BoxedArrayClass();
        primitive = ubiquity.map(boxed, PrimitiveArrayClass.class);

        assertNull(primitive.getBools());
        assertNull(primitive.getBytes());
        assertNull(primitive.getChars());
        assertNull(primitive.getDoubles());
        assertNull(primitive.getFloats());
        assertNull(primitive.getInts());
        assertNull(primitive.getLongs());
        assertNull(primitive.getShorts());
    }

    @Test
    public void testPrimitiveToBoxed() {
        PrimitiveArrayClass primitive = new PrimitiveArrayClass();
        primitive.setBools(new boolean[] {true, false, false});
        primitive.setBytes(new byte[] {0xc, 0xa, 0xf, 0xe, 0xb, 0xa, 0xb, 0xe});
        primitive.setChars(new char[] {'c', 'a', 'f', 'e', ' ', 'b', 'a', 'b', 'e'});
        primitive.setDoubles(new double[] {0d, 20d, 200d});
        primitive.setFloats(new float[]{1f, 5f});
        primitive.setInts(new int[]{0, 3, 6, 9});
        primitive.setLongs(new long[]{5l, 250l});
        primitive.setShorts(new short[]{9, 8, 7, 6});

        BoxedArrayClass boxed = ubiquity.map(primitive, BoxedArrayClass.class);

        assertNotNull(boxed.getBools());
        assertNotNull(boxed.getBytes());
        assertNotNull(boxed.getChars());
        assertNotNull(boxed.getDoubles());
        assertNotNull(boxed.getFloats());
        assertNotNull(boxed.getInts());
        assertNotNull(boxed.getLongs());
        assertNotNull(boxed.getShorts());

        assertEquals(primitive.getBools().length, boxed.getBools().length);
        assertEquals(primitive.getBytes().length, boxed.getBytes().length);
        assertEquals(primitive.getChars().length, boxed.getChars().length);
        assertEquals(primitive.getDoubles().length, boxed.getDoubles().length);
        assertEquals(primitive.getFloats().length, boxed.getFloats().length);
        assertEquals(primitive.getInts().length, boxed.getInts().length);
        assertEquals(primitive.getLongs().length, boxed.getLongs().length);
        assertEquals(primitive.getShorts().length, boxed.getShorts().length);

        for(int i = 0; i < primitive.getBools().length; i++) {
            assertEquals((Object)primitive.getBools()[i], boxed.getBools()[i]);
        }
        for(int i = 0; i < primitive.getBytes().length; i++) {
            assertEquals((Object)primitive.getBytes()[i], boxed.getBytes()[i]);
        }
        for(int i = 0; i < primitive.getChars().length; i++) {
            assertEquals((Object)primitive.getChars()[i], boxed.getChars()[i]);
        }
        for(int i = 0; i < primitive.getDoubles().length; i++) {
            assertEquals(primitive.getDoubles()[i], boxed.getDoubles()[i]);
        }
        for(int i = 0; i < primitive.getFloats().length; i++) {
            assertEquals(primitive.getFloats()[i], boxed.getFloats()[i]);
        }
        for(int i = 0; i < primitive.getInts().length; i++) {
            assertEquals((Object)primitive.getInts()[i], boxed.getInts()[i]);
        }
        for(int i = 0; i < primitive.getLongs().length; i++) {
            assertEquals((Object)primitive.getLongs()[i], boxed.getLongs()[i]);
        }
        for(int i = 0; i < primitive.getShorts().length; i++) {
            assertEquals((Object)primitive.getShorts()[i], boxed.getShorts()[i]);
        }
    }

    @Test
    public void testBoxedToPrimitive() {
        BoxedArrayClass boxed = new BoxedArrayClass();
        boxed.setBools(new Boolean[] {true, false, false});
        boxed.setBytes(new Byte[] {0xc, 0xa, 0xf, 0xe, 0xb, 0xa, 0xb, 0xe});
        boxed.setChars(new Character[] {'c', 'a', 'f', 'e', ' ', 'b', 'a', 'b', 'e'});
        boxed.setDoubles(new Double[] {0d, 20d, 200d});
        boxed.setFloats(new Float[]{1f, 5f});
        boxed.setInts(new Integer[]{0, 3, 6, 9});
        boxed.setLongs(new Long[]{5l, 250l});
        boxed.setShorts(new Short[]{9, 8, 7, 6});

        PrimitiveArrayClass primitive = ubiquity.map(boxed, PrimitiveArrayClass.class);

        assertNotNull(primitive.getBools());
        assertNotNull(primitive.getBytes());
        assertNotNull(primitive.getChars());
        assertNotNull(primitive.getDoubles());
        assertNotNull(primitive.getFloats());
        assertNotNull(primitive.getInts());
        assertNotNull(primitive.getLongs());
        assertNotNull(primitive.getShorts());

        assertEquals(boxed.getBools().length, primitive.getBools().length);
        assertEquals(boxed.getBytes().length, primitive.getBytes().length);
        assertEquals(boxed.getChars().length, primitive.getChars().length);
        assertEquals(boxed.getDoubles().length, primitive.getDoubles().length);
        assertEquals(boxed.getFloats().length, primitive.getFloats().length);
        assertEquals(boxed.getInts().length, primitive.getInts().length);
        assertEquals(boxed.getLongs().length, primitive.getLongs().length);
        assertEquals(boxed.getShorts().length, primitive.getShorts().length);

        for(int i = 0; i < boxed.getBools().length; i++) {
            assertEquals((Object)boxed.getBools()[i], primitive.getBools()[i]);
        }
        for(int i = 0; i < boxed.getBytes().length; i++) {
            assertEquals((Object)boxed.getBytes()[i], primitive.getBytes()[i]);
        }
        for(int i = 0; i < boxed.getChars().length; i++) {
            assertEquals((Object)boxed.getChars()[i], primitive.getChars()[i]);
        }
        for(int i = 0; i < boxed.getDoubles().length; i++) {
            assertEquals(boxed.getDoubles()[i], primitive.getDoubles()[i]);
        }
        for(int i = 0; i < boxed.getFloats().length; i++) {
            assertEquals(boxed.getFloats()[i], primitive.getFloats()[i]);
        }
        for(int i = 0; i < boxed.getInts().length; i++) {
            assertEquals((Object)boxed.getInts()[i], primitive.getInts()[i]);
        }
        for(int i = 0; i < boxed.getLongs().length; i++) {
            assertEquals((Object)boxed.getLongs()[i], primitive.getLongs()[i]);
        }
        for(int i = 0; i < boxed.getShorts().length; i++) {
            assertEquals((Object)boxed.getShorts()[i], primitive.getShorts()[i]);
        }
    }
}
