/**
 * 
 */
package org.ubiquity.bytecode;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.ubiquity.Copier;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Test for class {@link PropertyRetrieverVisitor}
 * 
 * @author François LAROCHE
 */
public class PropertyRetrieverVisitorTest {

	 @Test
     public void test() throws Exception {
//        long start = System.currentTimeMillis();
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/SimpleTestClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
//        long stop = System.currentTimeMillis();
//        System.out.println(visitor.toString());
//        System.out.println("ASM parsing took " + (stop - start) + "ms.");

    }

    @Test
    public void testInheritance() throws Exception {
//        long start = System.currentTimeMillis();
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/InheritingClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
//        long stop = System.currentTimeMillis();
//        System.out.println(visitor.toString());
//        System.out.println("ASM parsing took " + (stop - start) + "ms.");

    }
	
	@Test
	public void testBeanutils() throws Exception{
		Object dummy = new SimpleTestClass();
//		long start = System.currentTimeMillis();
		BeanUtils.describe(dummy);
//		long stop = System.currentTimeMillis();
//		System.out.println("beanutils parsing took " + (stop - start) + "ms.");
	}

    @Test
    public void testCopySpeed() throws Exception {
        SimpleTestClass src = new SimpleTestClass();
        src.setProperty1("property1");
        src.setProperty3("property3");
//        long start = System.currentTimeMillis();
        CopyContext ctx = new CopyContext();
        Copier<SimpleTestClass, SimpleTestClass> copier = new CopierGenerator().createCopier(SimpleTestClass.class, SimpleTestClass.class, ctx);
//        long start2 = System.currentTimeMillis();
        copier.copy(src, new SimpleTestClass());
//        long end = System.currentTimeMillis();
//        System.out.println("Asm process took : " + (end - start) + "ms, copy took " + (end - start2) + "ms");
//        start = System.currentTimeMillis();
        BeanUtils.copyProperties(new SimpleTestClass(), src);
//        end = System.currentTimeMillis();
//        System.out.println("BeanUtils process took : " + (end - start) + "ms");

    }

    @Test
    public void testCopierCreation() throws Exception {
        CopyContext ctx = new CopyContext();
        Copier<SimpleTestClass, SimpleTestClass> copier = new CopierGenerator().createCopier(SimpleTestClass.class, SimpleTestClass.class, ctx);
        assertNotNull(copier);
        SimpleTestClass testObject = new SimpleTestClass();
        String value = "This is a test";
        testObject.setProperty1("This is a test");
        SimpleTestClass result = copier.map(testObject);
        assertNotNull(result);
        assertEquals(value, result.getProperty1());
    }

    @Test
    public void testInternalClasses() throws Exception {
//        long start = System.currentTimeMillis();
        CopyContext ctx = new CopyContext();
        Copier<InheritingClass.InternalInheritingClass, InheritingClass.InternalInheritingClass> copier =
                new CopierGenerator().createCopier(InheritingClass.InternalInheritingClass.class, InheritingClass.InternalInheritingClass.class, ctx);
        assertNotNull(copier);
        InheritingClass.InternalInheritingClass testObject = new InheritingClass.InternalInheritingClass();
        testObject.setField(2);
        InheritingClass.InternalInheritingClass result = copier.map(testObject);
        assertEquals(Integer.valueOf(2), result.getField());
//        long end = System.currentTimeMillis();
//        System.out.println("testInternalClasses took " + (end - start) + "ms");
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

    @Test
    public void testObjectArrayCopy() throws Exception {
        ObjectArrayTest src = new ObjectArrayTest();
        ObjectArrayTest.ObjectArrayTest2 destination = new ObjectArrayTest.ObjectArrayTest2();
        ObjectArrayTest[] objects = new ObjectArrayTest[1];
        ObjectArrayTest obj = new ObjectArrayTest();
        objects[0] = obj;
        src.setObjects(objects);

        CopyContext ctx = new CopyContext();
        Copier<ObjectArrayTest, ObjectArrayTest.ObjectArrayTest2> copier =
                new CopierGenerator().createCopier(ObjectArrayTest.class, ObjectArrayTest.ObjectArrayTest2.class, ctx);

        copier.copy(src, destination);
        assertNotNull(destination.getObjects());
        assertEquals(1, destination.getObjects().length);
    }

    @Test
    public void testLists() throws Exception {
        ListObject src = new ListObject();
        List<SimpleTestClass> objects = new ArrayList<SimpleTestClass>();
        SimpleTestClass elem = new SimpleTestClass();
        objects.add(elem);
        src.setObjects(objects);

        CopyContext ctx = new CopyContext();
        Copier<ListObject, ListObject> copier =
                new CopierGenerator().createCopier(ListObject.class, ListObject.class, ctx);

        ListObject dest = copier.map(src);
        assertNotNull(dest);
        assertNotNull(dest.getObjects());
        assertEquals(1, dest.getObjects().size());
    }

    @Test
    public void testLists2() throws Exception {
        ListObject src = new ListObject();
        src.setObjects(null);

        CopyContext ctx = new CopyContext();
        Copier<ListObject, ListObject> copier =
                new CopierGenerator().createCopier(ListObject.class, ListObject.class, ctx);

        ListObject dest = copier.map(src);
        assertNotNull(dest);
        assertNull(dest.getObjects());
    }

    @Test
    public void testSimpleLists1() throws Exception {
        CollectionClass object = new CollectionClass();
        List<String> objects = new ArrayList<String>();
        objects.add("ubiquity");
        object.setObjects(objects);

        CopyContext ctx = new CopyContext();
        Copier<CollectionClass, CollectionClass> copier =
                new CopierGenerator().createCopier(CollectionClass.class, CollectionClass.class, ctx);

        CollectionClass dest = copier.map(object);
        assertNotNull(dest);
        assertNotNull(dest.getObjects());
        assertEquals(1, object.getObjects().size());
        assertEquals("ubiquity", object.getObjects().iterator().next());

    }

    @Test
    public void testSimpleLists2() throws Exception {
        CollectionClass object = new CollectionClass();
        object.setObjects(null);

        CopyContext ctx = new CopyContext();
        Copier<CollectionClass, CollectionClass> copier =
                new CopierGenerator().createCopier(CollectionClass.class, CollectionClass.class, ctx);

        CollectionClass dest = copier.map(object);
        assertNotNull(dest);
        assertNull(dest.getObjects());

    }

}
