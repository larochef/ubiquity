/**
 * 
 */
package org.ubiquity.bytecode;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

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
}
