/**
 * 
 */
package org.ubiquity.bytecode;

import java.io.IOException;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.ubiquity.Copier;

/**
 * @author François LAROCHE
 *
 */
public class CopierGenerator {
	
	private CopierGenerator() {}
	
	public enum PropertySearchType {
		READ,
		WRITE,
		BOTH
	}

	public static Map<String, Property> findProperties(PropertySearchType searchType, Class<?> clazz) {
		try {
			ClassReader reader = new ClassReader(bytecodeName(clazz));
			PropertyRetrieverVisitor visitor = new PropertyRetrieverVisitor();
			reader.accept(visitor, 0);
			return visitor.getProperties();
		} catch (IOException e) {
			throw new IllegalStateException("Unable to parse class : ", e);
		}
	}
	
	public static <T, U> Copier<T, U> createCopier(Class<T> src, Class<U> destination) {
		return null;
	}
	
	private static String bytecodeName(Class<?> c) {
		return c.getName().replaceAll("[\\.]", "/");
	}
}
