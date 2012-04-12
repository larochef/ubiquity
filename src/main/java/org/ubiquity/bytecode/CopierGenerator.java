/**
 * 
 */
package org.ubiquity.bytecode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
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

	public static List<Property> findProperties(PropertySearchType searchType, Class<?> clazz) {
		// Init ClassNode
		ClassNode cn = new ClassNode();
		try {
			ClassReader reader = new ClassReader(classToStream(clazz));
			reader.accept(cn, 0);
		} catch (IOException e) {
			return null;
		}
		
		List<Property> properties = new ArrayList<Property>();
		for(Object tmp : cn.fields) {
			FieldNode field = (FieldNode) tmp;
			
		}
		return properties;
	}
	
	public static <T, U> Copier<T, U> createCopier(Class<T> src, Class<U> destination) {
		return null;
	}
	
	private static InputStream classToStream(Class<?> c) {
		return CopierGenerator.class.getClassLoader().getResourceAsStream(c.getName().replaceAll("[\\.]", "/"));
	}
}
