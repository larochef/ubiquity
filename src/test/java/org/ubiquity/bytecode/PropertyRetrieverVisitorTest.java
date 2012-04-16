/**
 * 
 */
package org.ubiquity.bytecode;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.ubiquity.Copier;
import static junit.framework.Assert.*;

/**
 * Test for class {@link PropertyRetrieverVisitor}
 * 
 * @author François LAROCHE
 */
public class PropertyRetrieverVisitorTest {

	@Test
     public void test() throws Exception {
        long start = System.currentTimeMillis();
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/SimpleTestClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
        long stop = System.currentTimeMillis();
        System.out.println(visitor.toString());
        System.out.println("ASM parsing took " + (stop - start) + "ms.");

    }

    @Test
    public void testInheritance() throws Exception {
        long start = System.currentTimeMillis();
        ClassReader reader = new ClassReader("org/ubiquity/bytecode/InheritingClass");
        PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
        reader.accept(visitor, 0);
        long stop = System.currentTimeMillis();
        System.out.println(visitor.toString());
        System.out.println("ASM parsing took " + (stop - start) + "ms.");

    }
	
	@Test
	public void testBeanutils() throws Exception{
		Object dummy = new SimpleTestClass();
		long start = System.currentTimeMillis();
		BeanUtils.describe(dummy);
		long stop = System.currentTimeMillis();
		System.out.println("beanutils parsing took " + (stop - start) + "ms.");
	}

    @Test
    public void testCopierCreation() throws Exception {
        Copier<SimpleTestClass, SimpleTestClass> copier = CopierGenerator.createCopier(SimpleTestClass.class);
        assertNotNull(copier);
        SimpleTestClass testObject = new SimpleTestClass();
        String value = "This is a test";
        testObject.setProperty1("This is a test");
        SimpleTestClass result = copier.map(testObject);
        assertNotNull(result);
        assertEquals(value, result.getProperty1());
    }
}
