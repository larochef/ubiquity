package org.ubiquity.bytecode;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.ubiquity.Ubiquity;

/**
 * Date: 02/06/12
 *
 * @author François LAROCHE
 */
public class TestComplexObjectsArrays {
    private static final Ubiquity ubiquity = new Ubiquity();

    public static class SimpleObject1 {
        private int property;

        public int getProperty() {
            return property;
        }

        public void setProperty(int property) {
            this.property = property;
        }
    }

    public static class SimpleObject2 {
        private int property;

        public int getProperty() {
            return property;
        }

        public void setProperty(int property) {
            this.property = property;
        }
    }

    public static class ArrayClass1 {
        private SimpleObject1[] objects;

        public SimpleObject1[] getObjects() {
            return objects;
        }

        public void setObjects(SimpleObject1[] objects) {
            this.objects = objects;
        }
    }

    public static class ArrayClass2 {
        private SimpleObject2 [] objects;

        public SimpleObject2[] getObjects() {
            return objects;
        }

        public void setObjects(SimpleObject2[] objects) {
            this.objects = objects;
        }
    }

    @Test
    public void testNullArrays() {
        ArrayClass1 src = new ArrayClass1();
        ArrayClass2 dest = ubiquity.map(src, ArrayClass2.class);
        assertNull(dest.getObjects());
    }

    @Test
    public void testComplexArrays() {
        ArrayClass1 src = new ArrayClass1();
        SimpleObject1  obj1 = new SimpleObject1();
        obj1.setProperty(2);
        src.setObjects(new SimpleObject1[] {obj1});
        ArrayClass2 dest = ubiquity.map(src, ArrayClass2.class);
        assertNotNull(dest.getObjects());
        assertEquals(1, dest.getObjects().length);
        assertEquals(2, dest.getObjects()[0].getProperty());
    }
}
