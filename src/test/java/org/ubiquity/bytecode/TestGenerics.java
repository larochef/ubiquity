package org.ubiquity.bytecode;

import org.junit.Assert;
import org.junit.Test;
import org.ubiquity.Ubiquity;

/**
 * Date: 04/06/12
 *
 * @author François LAROCHE
 */
public class TestGenerics {
    private static final Ubiquity ubiquity = new Ubiquity();

    public static class ParentClass<T> {
        private T parent;

        public T getParent() {
            return parent;
        }

        public void setParent(T parent) {
            this.parent = parent;
        }
    }

    public static class TestClass {
        private ParentClass<ParentClass> element;

        public ParentClass<ParentClass> getElement() {
            return element;
        }

        public void setElement(ParentClass<ParentClass> element) {
            this.element = element;
        }
    }

    @Test
    public void testGenerics() {
        TestClass test = new TestClass();
        ParentClass<ParentClass> child = new ParentClass<ParentClass>();
        child.setParent(new ParentClass<ParentClass>());
        test.setElement(child);

        TestClass result = ubiquity.map(test, TestClass.class);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getElement());
        Assert.assertNotNull(result.getElement().getParent());
    }

}
