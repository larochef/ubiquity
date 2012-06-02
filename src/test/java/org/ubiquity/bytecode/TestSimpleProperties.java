package org.ubiquity.bytecode;

import org.junit.Test;
import org.ubiquity.Ubiquity;
import static org.junit.Assert.*;
/**
 * Date: 02/06/12
 *
 * @author François LAROCHE
 */
public class TestSimpleProperties {
    private static final Ubiquity ubiquity = new Ubiquity();

    public static class SimpleClass1 {
        private int myInt;
        private long myLong;
        private float myFloat;
        private double myDouble;
        private boolean myBoolean;
        private byte myByte;
        private short myShort;
        private char myChar;
        private Integer myIntObject;
        private Long myLongObject;
        private Float myFloatObject;
        private Double myDoubleObject;
        private Boolean myBooleanObject;
        private Byte myByteObject;
        private Short myShortObject;
        private Character myCharObject;
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
        public float getMyFloat() {
            return myFloat;
        }
        public void setMyFloat(float myFloat) {
            this.myFloat = myFloat;
        }
        public double getMyDouble() {
            return myDouble;
        }
        public void setMyDouble(double myDouble) {
            this.myDouble = myDouble;
        }
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
        public short getMyShort() {
            return myShort;
        }
        public void setMyShort(short myShort) {
            this.myShort = myShort;
        }
        public char getMyChar() {
            return myChar;
        }
        public void setMyChar(char myChar) {
            this.myChar = myChar;
        }
        public Integer getMyIntObject() {
            return myIntObject;
        }
        public void setMyIntObject(Integer myIntObject) {
            this.myIntObject = myIntObject;
        }
        public Long getMyLongObject() {
            return myLongObject;
        }
        public void setMyLongObject(Long myLongObject) {
            this.myLongObject = myLongObject;
        }
        public Float getMyFloatObject() {
            return myFloatObject;
        }
        public void setMyFloatObject(Float myFloatObject) {
            this.myFloatObject = myFloatObject;
        }
        public Double getMyDoubleObject() {
            return myDoubleObject;
        }
        public void setMyDoubleObject(Double myDoubleObject) {
            this.myDoubleObject = myDoubleObject;
        }
        public Boolean getMyBooleanObject() {
            return myBooleanObject;
        }
        public void setMyBooleanObject(Boolean myBooleanObject) {
            this.myBooleanObject = myBooleanObject;
        }
        public Byte getMyByteObject() {
            return myByteObject;
        }
        public void setMyByteObject(Byte myByteObject) {
            this.myByteObject = myByteObject;
        }
        public Short getMyShortObject() {
            return myShortObject;
        }
        public void setMyShortObject(Short myShortObject) {
            this.myShortObject = myShortObject;
        }
        public Character getMyCharObject() {
            return myCharObject;
        }
        public void setMyCharObject(Character myCharObject) {
            this.myCharObject = myCharObject;
        }
    }

    public static class SimpleClass2 {
        private int myInt;
        private long myLong;
        private float myFloat;
        private double myDouble;
        private boolean myBoolean;
        private byte myByte;
        private short myShort;
        private char myChar;
        private Integer myIntObject;
        private Long myLongObject;
        private Float myFloatObject;
        private Double myDoubleObject;
        private Boolean myBooleanObject;
        private Byte myByteObject;
        private Short myShortObject;
        private Character myCharObject;
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
        public float getMyFloat() {
            return myFloat;
        }
        public void setMyFloat(float myFloat) {
            this.myFloat = myFloat;
        }
        public double getMyDouble() {
            return myDouble;
        }
        public void setMyDouble(double myDouble) {
            this.myDouble = myDouble;
        }
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
        public short getMyShort() {
            return myShort;
        }
        public void setMyShort(short myShort) {
            this.myShort = myShort;
        }
        public char getMyChar() {
            return myChar;
        }
        public void setMyChar(char myChar) {
            this.myChar = myChar;
        }
        public Integer getMyIntObject() {
            return myIntObject;
        }
        public void setMyIntObject(Integer myIntObject) {
            this.myIntObject = myIntObject;
        }
        public Long getMyLongObject() {
            return myLongObject;
        }
        public void setMyLongObject(Long myLongObject) {
            this.myLongObject = myLongObject;
        }
        public Float getMyFloatObject() {
            return myFloatObject;
        }
        public void setMyFloatObject(Float myFloatObject) {
            this.myFloatObject = myFloatObject;
        }
        public Double getMyDoubleObject() {
            return myDoubleObject;
        }
        public void setMyDoubleObject(Double myDoubleObject) {
            this.myDoubleObject = myDoubleObject;
        }
        public Boolean getMyBooleanObject() {
            return myBooleanObject;
        }
        public void setMyBooleanObject(Boolean myBooleanObject) {
            this.myBooleanObject = myBooleanObject;
        }
        public Byte getMyByteObject() {
            return myByteObject;
        }
        public void setMyByteObject(Byte myByteObject) {
            this.myByteObject = myByteObject;
        }
        public Short getMyShortObject() {
            return myShortObject;
        }
        public void setMyShortObject(Short myShortObject) {
            this.myShortObject = myShortObject;
        }
        public Character getMyCharObject() {
            return myCharObject;
        }
        public void setMyCharObject(Character myCharObject) {
            this.myCharObject = myCharObject;
        }
    }

    @Test
    public void testSimpleObject() {
        SimpleClass1 obj1 = new SimpleClass1();
        SimpleClass2 obj2 = ubiquity.map(obj1, SimpleClass2.class);
        testEquality(obj1, obj2);

        obj1.setMyBoolean(true);
        obj1.setMyBooleanObject(true);
        obj1.setMyByte((byte)0x9);
        obj1.setMyByteObject((byte)0xA);
        obj1.setMyChar('Z');
        obj1.setMyCharObject('B');
        obj1.setMyDouble(25.2d);
        obj1.setMyDoubleObject(32.42);
        obj1.setMyFloat(1.5f);
        obj1.setMyFloatObject(3.6f);
        obj1.setMyInt(42);
        obj1.setMyIntObject(6);
        obj1.setMyLong(53L);
        obj1.setMyLongObject(123654789L);
        obj1.setMyShort((short)3);
        obj1.setMyShortObject((short)5);
        obj2 = ubiquity.map(obj1, SimpleClass2.class);
        testEquality(obj1, obj2);
    }

    private void testEquality(SimpleClass1 obj1, SimpleClass2 obj2) {
        assertEquals(obj1.isMyBoolean(), obj2.isMyBoolean());
        assertEquals(obj1.getMyBooleanObject(), obj2.getMyBooleanObject());
        assertEquals(obj1.getMyByte(), obj2.getMyByte());
        assertEquals(obj1.getMyByteObject(), obj2.getMyByteObject());
        assertEquals(obj1.getMyChar(), obj2.getMyChar());
        assertEquals(obj1.getMyCharObject(), obj2.getMyCharObject());
        assertEquals(obj1.getMyDouble(), obj2.getMyDouble(), 0.0001);
        assertEquals(obj1.getMyDoubleObject(), obj2.getMyDoubleObject());
        assertEquals(obj1.getMyFloat(), obj2.getMyFloat(), 0.0001);
        assertEquals(obj1.getMyFloatObject(), obj2.getMyFloatObject());
        assertEquals(obj1.getMyInt(), obj2.getMyInt());
        assertEquals(obj1.getMyIntObject(), obj2.getMyIntObject());
        assertEquals(obj1.getMyLong(), obj2.getMyLong());
        assertEquals(obj1.getMyLongObject(), obj2.getMyLongObject());
        assertEquals(obj1.getMyShort(), obj2.getMyShort());
        assertEquals(obj1.getMyShortObject(), obj2.getMyShortObject());
    }
}
