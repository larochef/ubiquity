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
public class TestSimpleCollections {

    private static final Ubiquity ubiquity = new Ubiquity();

    public static class CollectionsClass {
        private Collection<String> collection;
        private List<String> list;
        private Set<String> set;
        private Map<String, String> map;

        public Collection<String> getCollection() {
            return collection;
        }

        public void setCollection(Collection<String> collection) {
            this.collection = collection;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public Set<String> getSet() {
            return set;
        }

        public void setSet(Set<String> set) {
            this.set = set;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }

    @Test
    public void testNulCollections() {

        TestSimpleCollections.CollectionsClass object = new TestSimpleCollections.CollectionsClass();
        TestSimpleCollections.CollectionsClass result = ubiquity.map(object, TestSimpleCollections.CollectionsClass.class);

        assertNotNull(result);
        assertNull(result.getCollection());
        assertNull(result.getList());
        assertNull(result.getSet());
        assertNull(result.getMap());

        result.setCollection(new HashSet<String>());
        result.setList(new ArrayList<String>());
        result.setSet(new HashSet<String>());
        result.setMap(new HashMap<String, String>());

        ubiquity.copy(object, result);

        assertNull(result.getCollection());
        assertNull(result.getList());
        assertNull(result.getSet());
        assertNull(result.getMap());
    }

    @Test
    public void testEmptyCollections() {
        TestSimpleCollections.CollectionsClass object = new TestSimpleCollections.CollectionsClass();
        object.setCollection(new HashSet<String>());
        object.setList(new LinkedList<String>());
        object.setSet(new TreeSet<String>());
        object.setMap(new HashMap<String, String>());

        TestSimpleCollections.CollectionsClass result = ubiquity.map(object, TestSimpleCollections.CollectionsClass.class);
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
        TestSimpleCollections.CollectionsClass object = new TestSimpleCollections.CollectionsClass();
        object.setCollection(new HashSet<String>());
        object.setList(new LinkedList<String>());
        object.setSet(new TreeSet<String>());
        object.setMap(new HashMap<String, String>());

        object.getCollection().add("collection");
        object.getList().add("list");
        object.getSet().add("set");
        object.getMap().put("key", "value");

        TestSimpleCollections.CollectionsClass result = ubiquity.map(object, TestSimpleCollections.CollectionsClass.class);
        assertNotNull(result);
        assertNotNull(result.getCollection());
        assertNotNull(result.getList());
        assertNotNull(result.getSet());
        assertNotNull(result.getMap());

        assertTrue(result.getCollection().contains("collection"));
        assertTrue(result.getList().contains("list"));
        assertTrue(result.getSet().contains("set"));
        assertTrue("value".equals(result.getMap().get("key")));

        CollectionFactory factory = ubiquity.getFactory();
        assertEquals(factory.newCollection().getClass(), result.getCollection().getClass());
        assertEquals(factory.newList().getClass(), result.getList().getClass());
        assertEquals(factory.newSet().getClass(), result.getSet().getClass());
        assertEquals(factory.newMap().getClass(), result.getMap().getClass());
    }
}
