/**
 * 
 */
package org.ubiquity.bytecode;

import org.junit.Test;
import org.ubiquity.Copier;
import org.ubiquity.annotation.CopyIgnore;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Test for class {@link PropertyRetrieverVisitor}
 * 
 * @author François LAROCHE
 */
public class PropertyRetrieverVisitorTest {

    @Test
    public void testInternalClasses() throws Exception {
        CopyContext ctx = new CopyContext();
        Copier<InheritingClass.InternalInheritingClass, InheritingClass.InternalInheritingClass> copier =
                new CopierGenerator().createCopier(InheritingClass.InternalInheritingClass.class, InheritingClass.InternalInheritingClass.class, ctx);
        assertNotNull(copier);
        InheritingClass.InternalInheritingClass testObject = new InheritingClass.InternalInheritingClass();
        testObject.setField(2);
        InheritingClass.InternalInheritingClass result = copier.map(testObject);
        assertEquals(Integer.valueOf(2), result.getField());
    }

    @Test
    public void testMultipleImplementations() throws Exception {
        CopyContext ctx = new CopyContext();
        Copier<InheritingClass, InheritingClass2> copier =
                new CopierGenerator().createCopier(InheritingClass.class, InheritingClass2.class, ctx);
        InheritingClass src = new InheritingClass();
        src.setMyProperty("My property");
        src.setProperty1("property1");
        InheritingClass parent = new InheritingClass();
        parent.setProperty1("Parent property 1");
        src.setParent(parent);

        InheritingClass2 result = copier.map(src);
        assertNotNull(result);
        assertEquals("property1", result.getProperty1());
        assertNotNull(result.getParent());
        assertEquals("Parent property 1", result.getParent().getProperty1());
    }

    public static class InheritingClass extends SimpleTestClass {

        private String myProperty;
        private InheritingClass parent;

        public String getMyProperty() {
            return myProperty;
        }



        public void setMyProperty(String myProperty) {
            this.myProperty = myProperty;
        }

        public InheritingClass getParent() {
            return parent;
        }

        public void setParent(InheritingClass parent) {
            this.parent = parent;
        }

        public static class InternalInheritingClass extends InheritingClass {

            public InternalInheritingClass() {}

            private Integer field;

            public Integer getField() {
                return field;
            }

            public void setField(Integer field) {
                this.field = field;
            }
        }
    }

    public static class InheritingClass2 extends SimpleTestClass {

        private InheritingClass2 parent;

        public InheritingClass2 getParent() {
            return parent;
        }

        public void setParent(InheritingClass2 parent) {
            this.parent = parent;
        }
    }

    public static class SimpleTestClass {

        private String property1;
        private String property2;
        private String property3;
        private String property4;

        public String getProperty1() {
            return property1;
        }
        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        @CopyIgnore
        public String getProperty2() {
            return property2;
        }
        public void setProperty3(String property3) {
            this.property3 = property3;
        }
        @Override
        public String toString() {
            return "SimpleObject [property1=" + property1 + ", property2="
                    + property2 + ", property3=" + property3 + ", property4="
                    + property4 + "]";
        }

    }


}
