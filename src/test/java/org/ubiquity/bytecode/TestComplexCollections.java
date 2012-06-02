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

    public static class SimpleObject {

        private String property1;

        public String getProperty1() {
            return property1;
        }
        public void setProperty1(String property1) {
            this.property1 = property1;
        }
    }

    public static class CollectionsClass {
        private Collection<SimpleObject> collection;
        private List<SimpleObject> list;
        private Set<SimpleObject> set;
        private Map<String, SimpleObject> map;

        public Collection<SimpleObject> getCollection() {
            return collection;
        }

        public void setCollection(Collection<SimpleObject> collection) {
            this.collection = collection;
        }

        public List<SimpleObject> getList() {
            return list;
        }

        public void setList(List<SimpleObject> list) {
            this.list = list;
        }

        public Set<SimpleObject> getSet() {
            return set;
        }

        public void setSet(Set<SimpleObject> set) {
            this.set = set;
        }

        public Map<String, SimpleObject> getMap() {
            return map;
        }

        public void setMap(Map<String, SimpleObject> map) {
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

        result.setCollection(new HashSet<SimpleObject>());
        result.setList(new ArrayList<SimpleObject>());
        result.setSet(new HashSet<SimpleObject>());
        result.setMap(new HashMap<String, SimpleObject>());

        ubiquity.copy(object, result);

        assertNull(result.getCollection());
        assertNull(result.getList());
        assertNull(result.getSet());
        assertNull(result.getMap());
    }

    @Test
    public void testEmptyCollections() {
        TestComplexCollections.CollectionsClass object = new TestComplexCollections.CollectionsClass();
        object.setCollection(new HashSet<SimpleObject>());
        object.setList(new LinkedList<SimpleObject>());
        object.setSet(new TreeSet<SimpleObject>());
        object.setMap(new HashMap<String, SimpleObject>());

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
        object.setCollection(new HashSet<SimpleObject>());
        object.setList(new LinkedList<SimpleObject>());
        object.setSet(new TreeSet<SimpleObject>());
        object.setMap(new HashMap<String, SimpleObject>());

        SimpleObject elem = new SimpleObject();
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
