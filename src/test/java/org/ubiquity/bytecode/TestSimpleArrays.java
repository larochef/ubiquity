package org.ubiquity.bytecode;

import org.junit.Test;
import org.ubiquity.Ubiquity;
import static junit.framework.Assert.*;
/**
 * Date: 01/06/12
 *
 * @author François LAROCHE
 */
public class TestSimpleArrays {
    private static final Ubiquity ubiquity = new Ubiquity();

    // Class containing all the possible arrays
    public static class ArraysTest {
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

    @Test
    public void testNullArrays() {
        ArraysTest obj = new ArraysTest();
        ArraysTest dest = ubiquity.map(obj, ArraysTest.class);

        assertNull(dest.getBoolObjects());
        assertNull(dest.getBools());
        assertNull(dest.getByteObjects());
        assertNull(dest.getBytes());
        assertNull(dest.getCharObjects());
        assertNull(dest.getChars());
        assertNull(dest.getDoubleObjects());
        assertNull(dest.getDoubles());
        assertNull(dest.getFloatObjects());
        assertNull(dest.getFloats());
        assertNull(dest.getIntObjects());
        assertNull(dest.getInts());
        assertNull(dest.getLongObjects());
        assertNull(dest.getLongs());
        assertNull(dest.getShortObjects());
        assertNull(dest.getShorts());
    }

    @Test
    public void testDatas() {
        ArraysTest obj = new ArraysTest();
        obj.setBoolObjects(new Boolean[] {true, false, false, false, true});
        obj.setBools(new boolean[] {true, true, true, false});
        obj.setByteObjects(new Byte[] {0x10, 0x0A, 0xF});
        obj.setBytes(new byte[] {0xB, 0x55, 0x0, 0x2D});
        obj.setCharObjects(new Character[] {'A', 'z', 'e'});
        obj.setChars(new char[] {'Q', 's', 'D', 'w'});
        obj.setDoubleObjects(new Double[] {1d, 2512589d, 11.1236d});
        obj.setDoubles(new double[] {0d, 3.14d});
        obj.setFloatObjects(new Float[] {1.2f});
        obj.setFloats(new float[] {1.5f, 2.45f});
        obj.setIntObjects(new Integer[] {2, 3, 4});
        obj.setInts(new int[] {});
        obj.setLongObjects(new Long[] {2222255558787744112L, 258L});
        obj.setLongs(new long[] {0L});
        obj.setShortObjects(new Short[] {2});
        obj.setShorts(new short[] {3, 4, 5});
        ArraysTest dest = ubiquity.map(obj, ArraysTest.class);

        assertNotNull(dest.getBoolObjects());
        assertNotNull(dest.getBools());
        assertNotNull(dest.getByteObjects());
        assertNotNull(dest.getBytes());
        assertNotNull(dest.getCharObjects());
        assertNotNull(dest.getChars());
        assertNotNull(dest.getDoubleObjects());
        assertNotNull(dest.getDoubles());
        assertNotNull(dest.getFloatObjects());
        assertNotNull(dest.getFloats());
        assertNotNull(dest.getIntObjects());
        assertNotNull(dest.getInts());
        assertNotNull(dest.getLongObjects());
        assertNotNull(dest.getLongs());
        assertNotNull(dest.getShortObjects());
        assertNotNull(dest.getShorts());

        assertEquals(obj.getBoolObjects().length, dest.getBoolObjects().length);
        assertEquals(obj.getBools().length, dest.getBools().length);
        assertEquals(obj.getByteObjects().length, dest.getByteObjects().length);
        assertEquals(obj.getBytes().length, dest.getBytes().length);
        assertEquals(obj.getCharObjects().length, dest.getCharObjects().length);
        assertEquals(obj.getChars().length, dest.getChars().length);
        assertEquals(obj.getDoubleObjects().length, dest.getDoubleObjects().length);
        assertEquals(obj.getDoubles().length, dest.getDoubles().length);
        assertEquals(obj.getFloatObjects().length, dest.getFloatObjects().length);
        assertEquals(obj.getFloats().length, dest.getFloats().length);
        assertEquals(obj.getIntObjects().length, dest.getIntObjects().length);
        assertEquals(obj.getInts().length, dest.getInts().length);
        assertEquals(obj.getLongObjects().length, dest.getLongObjects().length);
        assertEquals(obj.getLongs().length, dest.getLongs().length);
        assertEquals(obj.getShortObjects().length, dest.getShortObjects().length);
        assertEquals(obj.getShorts().length, dest.getShorts().length);

        for(int i = 0; i < obj.getBoolObjects().length; i++) {
            assertEquals(obj.getBoolObjects()[i], dest.getBoolObjects()[i]);
        }
        for(int i = 0; i < obj.getBools().length; i++) {
            assertEquals(obj.getBools()[i], dest.getBools()[i]);
        }
        for(int i = 0; i < obj.getByteObjects().length; i++) {
            assertEquals(obj.getByteObjects()[i], dest.getByteObjects()[i]);
        }
        for(int i = 0; i < obj.getBytes().length; i++) {
            assertEquals(obj.getBytes()[i], dest.getBytes()[i]);
        }
        for(int i = 0; i < obj.getCharObjects().length; i++) {
            assertEquals(obj.getCharObjects()[i], dest.getCharObjects()[i]);
        }
        for(int i = 0; i < obj.getChars().length; i++) {
            assertEquals(obj.getChars()[i], dest.getChars()[i]);
        }
        for(int i = 0; i < obj.getDoubleObjects().length; i++) {
            assertEquals(obj.getDoubleObjects()[i], dest.getDoubleObjects()[i]);
        }
        for(int i = 0; i < obj.getDoubles().length; i++) {
            assertEquals(obj.getDoubles()[i], dest.getDoubles()[i]);
        }
        for(int i = 0; i < obj.getFloatObjects().length; i++) {
            assertEquals(obj.getFloatObjects()[i], dest.getFloatObjects()[i]);
        }
        for(int i = 0; i < obj.getFloats().length; i++) {
            assertEquals(obj.getFloats()[i], dest.getFloats()[i]);
        }
        for(int i = 0; i < obj.getIntObjects().length; i++) {
            assertEquals(obj.getIntObjects()[i], dest.getIntObjects()[i]);
        }
        for(int i = 0; i < obj.getInts().length; i++) {
            assertEquals(obj.getInts()[i], dest.getInts()[i]);
        }
        for(int i = 0; i < obj.getLongObjects().length; i++) {
            assertEquals(obj.getLongObjects()[i], dest.getLongObjects()[i]);
        }
        for(int i = 0; i < obj.getLongs().length; i++) {
            assertEquals(obj.getLongs()[i], dest.getLongs()[i]);
        }
        for(int i = 0; i < obj.getShortObjects().length; i++) {
            assertEquals(obj.getShortObjects()[i], dest.getShortObjects()[i]);
        }
        for(int i = 0; i < obj.getShorts().length; i++) {
            assertEquals(obj.getShorts()[i], dest.getShorts()[i]);
        }
    }
}
