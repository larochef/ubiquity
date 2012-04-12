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
	
	private static final String EMPTY_SIGNATURE = "()";
	private static final String BOOLEAN_GETTER_SIGNATURE = "()Z";
	private static final String VOID = "V";
	private static final String GET = "get";
	private static final String SET = "set";
	private static final String IS = "is";

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
		if(isGetterOrSetter(name, desc)) {
			Property property = getProperty(getPropertyName(name));
			char start = name.charAt(0);
			if(start == 's') {
				property.setSetter(name);
			}
			else {
				property.setGetter(name);
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
	
	private static boolean isSingleParameter(String signature) {
		int start = 1;
		int end = signature.indexOf(")");
		if(signature.charAt(start) == '[') {
			start ++;
		}
		if(signature.charAt(start) == 'L') {
			start = signature.indexOf(';');
		}
		start ++;
		return start == end;
	}
	
	private static boolean isSetter(String name, String signature) {
		if(name == null || signature == null) {
			return false;
		}
		if(!signature.endsWith(VOID)) {
			return false;
		}
		if(!name.startsWith(SET)) {
			return false;
		}
		if(name.length() < 4) {
			return false;
		}
		if(!isUppercase(name.charAt(3))) {
			return false;
		}
		// last test : test if the function only accepts one parameter
		return isSingleParameter(signature);
	}

	private static boolean isGetter(String name, String signature) {
		if(name == null || signature == null) {
			return false;
		}
		if(!signature.startsWith(EMPTY_SIGNATURE)) {
			return false;
		}
		if(signature.endsWith(VOID)) {
			return false;
		}
		// case for boolean, and not Boolean
		if(name.startsWith(IS) && BOOLEAN_GETTER_SIGNATURE.equals(signature)) {
			if(name.length() < 3) {
				return false;
			}
			return isUppercase(name.charAt(2));
		}
		if(name.startsWith(GET)) {
			if(name.length() < 4) {
				return false;
			}
			return isUppercase(name.charAt(3));
		}
		return false;
	}
	
	private static boolean isUppercase(char c) {
		return c >= 'A' && c <= 'Z';
	}

	private static boolean isGetterOrSetter(String name, String signature) {
		return isGetter(name, signature) || isSetter(name, signature);
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

	public Map<String, Property> getProperties() {
		return properties;
	}

}
