package org.ubiquity.bytecode;

import org.junit.Test;
import org.ubiquity.CollectionFactory;
import org.ubiquity.Ubiquity;

import java.util.*;

import static junit.framework.Assert.*;

/**
 * This class is dedicated to the test with collections
 *
 * Date: 31/05/12
 *
 * @author François LAROCHE
 */
public class TestComplexCollections {

    private static final Ubiquity ubiquity = new Ubiquity();

    public static class CollectionsClass {
        private Collection<SimpleTestClass> collection;
        private List<SimpleTestClass> list;
        private Set<SimpleTestClass> set;
        private Map<String, SimpleTestClass> map;

        public Collection<SimpleTestClass> getCollection() {
            return collection;
        }

        public void setCollection(Collection<SimpleTestClass> collection) {
            this.collection = collection;
        }

        public List<SimpleTestClass> getList() {
            return list;
        }

        public void setList(List<SimpleTestClass> list) {
            this.list = list;
        }

        public Set<SimpleTestClass> getSet() {
            return set;
        }

        public void setSet(Set<SimpleTestClass> set) {
            this.set = set;
        }

        public Map<String, SimpleTestClass> getMap() {
            return map;
        }

        public void setMap(Map<String, SimpleTestClass> map) {
            this.map = map;
        }
    }

    @Test
    public void testNulCollections() {

        TestComplexCollections.CollectionsClass object = new TestComplexCollections.CollectionsClass();
        TestComplexCollections.CollectionsClass result = ubiquity.map(object, TestComplexCollections.CollectionsClass.class);

        assertNotNull(result);
        assertNull(result.getCollection());
        assertNull(result.getList());
        assertNull(result.getSet());
        assertNull(result.getMap());

        result.setCollection(new HashSet<SimpleTestClass>());
        result.setList(new ArrayList<SimpleTestClass>());
        result.setSet(new HashSet<SimpleTestClass>());
        result.setMap(new HashMap<String, SimpleTestClass>());

        ubiquity.copy(object, result);

        assertNull(result.getCollection());
        assertNull(result.getList());
        assertNull(result.getSet());
        assertNull(result.getMap());
    }

    @Test
    public void testEmptyCollections() {
        TestComplexCollections.CollectionsClass object = new TestComplexCollections.CollectionsClass();
        object.setCollection(new HashSet<SimpleTestClass>());
        object.setList(new LinkedList<SimpleTestClass>());
        object.setSet(new TreeSet<SimpleTestClass>());
        object.setMap(new HashMap<String, SimpleTestClass>());

        TestComplexCollections.CollectionsClass result = ubiquity.map(object, TestComplexCollections.CollectionsClass.class);
        assertNotNull(result);
        assertNotNull(result.getCollection());
        assertNotNull(result.getList());
        assertNotNull(result.getSet());
        assertNotNull(result.getMap());

        assertTrue(result.getCollection().isEmpty());
        assertTrue(result.getList().isEmpty());
        assertTrue(result.getSet().isEmpty());
        assertTrue(result.getMap().isEmpty());

        CollectionFactory factory = ubiquity.getFactory();
        assertEquals(factory.newCollection().getClass(), result.getCollection().getClass());
        assertEquals(factory.newList().getClass(), result.getList().getClass());
        assertEquals(factory.newSet().getClass(), result.getSet().getClass());
        assertEquals(factory.newMap().getClass(), result.getMap().getClass());
    }

    @Test
    public void testNonEmptyCollections() {
        TestComplexCollections.CollectionsClass object = new TestComplexCollections.CollectionsClass();
        object.setCollection(new HashSet<SimpleTestClass>());
        object.setList(new LinkedList<SimpleTestClass>());
        object.setSet(new TreeSet<SimpleTestClass>());
        object.setMap(new HashMap<String, SimpleTestClass>());

        SimpleTestClass elem = new SimpleTestClass();
        elem.setProperty1("property");

        object.getCollection().add(elem);
        object.getList().add(elem);
        object.getSet().add(elem);
        object.getMap().put("key", elem);

        TestComplexCollections.CollectionsClass result = ubiquity.map(object, TestComplexCollections.CollectionsClass.class);
        assertNotNull(result);
        assertNotNull(result.getCollection());
        assertNotNull(result.getList());
        assertNotNull(result.getSet());
        assertNotNull(result.getMap());

        assertTrue(result.getCollection().size() == 1);
        assertTrue(result.getList().size() == 1);
        assertTrue(result.getSet().size() == 1);
        assertTrue(result.getMap().size() == 1);

        assertTrue("property".equals(result.getCollection().iterator().next().getProperty1()));
        assertTrue("property".equals(result.getList().iterator().next().getProperty1()));
        assertTrue("property".equals(result.getSet().iterator().next().getProperty1()));
        assertTrue("property".equals(result.getMap().get("key").getProperty1()));

        CollectionFactory factory = ubiquity.getFactory();
        assertEquals(factory.newCollection().getClass(), result.getCollection().getClass());
        assertEquals(factory.newList().getClass(), result.getList().getClass());
        assertEquals(factory.newSet().getClass(), result.getSet().getClass());
        assertEquals(factory.newMap().getClass(), result.getMap().getClass());
    }
}
