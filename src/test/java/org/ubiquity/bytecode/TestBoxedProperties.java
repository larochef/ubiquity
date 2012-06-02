package org.ubiquity.bytecode;

import org.junit.Test;
import org.ubiquity.Ubiquity;
import static org.junit.Assert.*;
/**
 * Date: 02/06/12
 *
 * @author François LAROCHE
 */
public class TestBoxedProperties {

    private static final Ubiquity ubiquity = new Ubiquity();

    public static class PrimitiveClass {
        private boolean myBoolean;
        private byte myByte;
        private char myCharacter;
        private double myDouble;
        private float myFloat;
        private int myInt;
        private long myLong;
        private short myShort;

        public boolean isMyBoolean() {
            return myBoolean;
        }

        public void setMyBoolean(boolean myBoolean) {
            this.myBoolean = myBoolean;
        }

        public byte getMyByte() {
            return myByte;
        }

        public void setMyByte(byte myByte) {
            this.myByte = myByte;
        }

        public char getMyCharacter() {
            return myCharacter;
        }

        public void setMyCharacter(char myCharacter) {
            this.myCharacter = myCharacter;
        }

        public double getMyDouble() {
            return myDouble;
        }

        public void setMyDouble(double myDouble) {
            this.myDouble = myDouble;
        }

        public float getMyFloat() {
            return myFloat;
        }

        public void setMyFloat(float myFloat) {
            this.myFloat = myFloat;
        }

        public int getMyInt() {
            return myInt;
        }

        public void setMyInt(int myInt) {
            this.myInt = myInt;
        }

        public long getMyLong() {
            return myLong;
        }

        public void setMyLong(long myLong) {
            this.myLong = myLong;
        }

        public short getMyShort() {
            return myShort;
        }

        public void setMyShort(short myShort) {
            this.myShort = myShort;
        }
    }

    public static class BoxedClass {
        private Boolean myBoolean;
        private Byte myByte;
        private Character myCharacter;
        private Double myDouble;
        private Float myFloat;
        private Integer myInt;
        private Long myLong;
        private Short myShort;

        public Boolean getMyBoolean() {
            return myBoolean;
        }

        public void setMyBoolean(Boolean myBoolean) {
            this.myBoolean = myBoolean;
        }

        public Byte getMyByte() {
            return myByte;
        }

        public void setMyByte(Byte myByte) {
            this.myByte = myByte;
        }

        public Character getMyCharacter() {
            return myCharacter;
        }

        public void setMyCharacter(Character myCharacter) {
            this.myCharacter = myCharacter;
        }

        public Double getMyDouble() {
            return myDouble;
        }

        public void setMyDouble(Double myDouble) {
            this.myDouble = myDouble;
        }

        public Float getMyFloat() {
            return myFloat;
        }

        public void setMyFloat(Float myFloat) {
            this.myFloat = myFloat;
        }

        public Integer getMyInt() {
            return myInt;
        }

        public void setMyInt(Integer myInt) {
            this.myInt = myInt;
        }

        public Long getMyLong() {
            return myLong;
        }

        public void setMyLong(Long myLong) {
            this.myLong = myLong;
        }

        public Short getMyShort() {
            return myShort;
        }

        public void setMyShort(Short myShort) {
            this.myShort = myShort;
        }
    }

    @Test
    public void testNullBoxing() {
        BoxedClass boxed = new BoxedClass();
        PrimitiveClass primitive = ubiquity.map(boxed, PrimitiveClass.class);
        assertEquals(false, primitive.isMyBoolean());
        assertEquals(0, primitive.getMyByte());
        assertEquals(0, primitive.getMyCharacter());
        assertEquals(0, primitive.getMyInt());
        assertEquals(0, primitive.getMyShort());
        assertEquals(0, primitive.getMyLong());
        assertEquals(0, primitive.getMyFloat(), 0.01);
        assertEquals(0, primitive.getMyDouble(), 0.01);
    }

    @Test
    public void testPrimitiveToBoxed() {
        PrimitiveClass primitive = new PrimitiveClass();
        primitive.setMyBoolean(true);
        primitive.setMyByte((byte)0xC);
        primitive.setMyCharacter('F');
        primitive.setMyDouble(123.321d);
        primitive.setMyFloat(21.21f);
        primitive.setMyInt(42);
        primitive.setMyLong(123456789L);
        primitive.setMyShort((short)3);

        BoxedClass boxed = ubiquity.map(primitive, BoxedClass.class);
        checkEquality(primitive, boxed);
    }

    @Test
    public void testBoxedToPrimitive() {
        BoxedClass boxed = new BoxedClass();
        boxed.setMyBoolean(true);
        boxed.setMyByte((byte) 0xC);
        boxed.setMyCharacter('F');
        boxed.setMyDouble(123.321d);
        boxed.setMyFloat(21.21f);
        boxed.setMyInt(42);
        boxed.setMyLong(123456789L);
        boxed.setMyShort((short) 3);

        PrimitiveClass primitive = ubiquity.map(boxed, PrimitiveClass.class);
        checkEquality(primitive, boxed);

    }

    private void checkEquality(PrimitiveClass primitive, BoxedClass boxed) {
        assertEquals(primitive.isMyBoolean(), boxed.getMyBoolean());
        assertEquals(primitive.getMyByte(), boxed.getMyByte().byteValue());
        assertEquals(primitive.getMyCharacter(), boxed.getMyCharacter().charValue());
        assertEquals(primitive.getMyDouble(), boxed.getMyDouble(), 0.001);
        assertEquals(primitive.getMyFloat(), boxed.getMyFloat(), 0.001);
        assertEquals(primitive.getMyInt(), boxed.getMyInt().intValue());
        assertEquals(primitive.getMyLong(), boxed.getMyLong().longValue());
        assertEquals(primitive.getMyShort(), boxed.getMyShort().shortValue());
    }
}
