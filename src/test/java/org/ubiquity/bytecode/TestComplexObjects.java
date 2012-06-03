package org.ubiquity.bytecode;

import org.junit.Test;
import org.ubiquity.Ubiquity;
import static org.junit.Assert.*;
/**
 * Date: 03/06/12
 *
 * @author François LAROCHE
 */
public class TestComplexObjects {
    private static final Ubiquity ubiquity = new Ubiquity();

    public static class ComplexClass1 {
        private ComplexClass1 parent;

        public ComplexClass1 getParent() {
            return parent;
        }

        public void setParent(ComplexClass1 parent) {
            this.parent = parent;
        }
    }

    public static class ComplexClass2 {
        private ComplexClass2 parent;

        public ComplexClass2 getParent() {
            return parent;
        }

        public void setParent(ComplexClass2 parent) {
            this.parent = parent;
        }
    }

    @Test
    public void testComplexCopies() {
        ComplexClass1 src = new ComplexClass1();
        ComplexClass2 dest = ubiquity.map(src, ComplexClass2.class);
        assertNotNull(dest);
        assertNull(dest.getParent());
        src.setParent(new ComplexClass1());
        dest = ubiquity.map(src, ComplexClass2.class);
        assertNotNull(dest);
        assertNotNull(dest.getParent());
    }
}
