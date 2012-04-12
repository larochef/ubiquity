/**
 * 
 */
package org.ubiquity.bytecode;

import static org.ubiquity.util.Constants.ASM_LEVEL;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @author François LAROCHE
 *
 */
public final class PropertyRetrieverVisitor extends ClassVisitor {

	private final Map<String, Property> properties;

	public PropertyRetrieverVisitor() {
		super(ASM_LEVEL);
		this.properties = new HashMap<String, Property>();
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		Property property = getProperty(name);
		property.setName(name);
		return new FieldReader(property);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if(isGetterOrSetter(name)) {
			Property property = getProperty(getPropertyName(name));
			char start = name.charAt(0);
			if(start == 'g') {
				property.setGetter(name);
			}
			else {
				property.setSetter(name);
			}
			return new MethodReader(property);
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	private Property getProperty(String name) {
		if(!this.properties.containsKey(name)) {
			this.properties.put(name, new Property());
		}
		return this.properties.get(name);
	}
	
	private String getPropertyName(String functionName) {
		StringBuilder build = new StringBuilder()
		// lower case
		.append((char)(functionName.charAt(3) - 'A' + 'a'));
		if(functionName.length() > 4) {
			build.append(functionName.substring(4));
		}
		return build.toString();
		
	}

	private static boolean isGetterOrSetter(String name) {
		if(name.length() < 4) {
			return false;
		}
		if(name.charAt(1) != 'e') {
			return false;
		}
		if(name.charAt(2) != 't') {
			return false;
		}
		char start = name.charAt(0);
		if(start != 'g' && start != 's') {
			return false;
		}
		char propertyStart = name.charAt(3);
		return propertyStart >= 'A' && propertyStart <= 'Z';
	}

	private static class FieldReader extends FieldVisitor {
		private final Property property;
		
		private FieldReader(Property property) {
			super(ASM_LEVEL);
			this.property = property;
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			if(visible) {
				this.property.getAnnotations().add(desc);
			}
			return super.visitAnnotation(desc, visible);
		}
	}
	
	private final class MethodReader extends MethodVisitor {
		
		private final Property property;
		
		private MethodReader(Property property) {
			super(ASM_LEVEL);
			this.property = property;
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			if(visible) {
				this.property.getAnnotations().add(desc);
			}
			return super.visitAnnotation(desc, visible);
		}

	}
	
	@Override public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PropertyRetrieverVisitor {");
		for(String key : this.properties.keySet()) {
			builder.append('\n')
			.append("\t- Property ")
			.append(key)
			.append(" : ")
			.append(this.properties.get(key))
			;
		}
		builder.append('}');
		return builder.toString();
	}
	
}
