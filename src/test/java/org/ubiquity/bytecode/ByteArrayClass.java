package org.ubiquity.bytecode;

/**
 * Date: 26/05/12
 *
 * @author François LAROCHE
 */
public class ByteArrayClass {
    byte[] array;

    public byte[] getArray() {
        return array;
    }

    public void setArray(byte[] array) {
        this.array = array;
    }

    public static class ByteArray2 {
        Byte[] array;

        public Byte[] getArray() {
            return array;
        }

        public void setArray(Byte[] array) {
            this.array = array;
        }
    }
}
