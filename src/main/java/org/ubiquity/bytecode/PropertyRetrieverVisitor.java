/**
 * 
 */
package org.ubiquity.bytecode;

import org.objectweb.asm.*;
import org.ubiquity.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ubiquity.util.Constants.ASM_LEVEL;
import static org.ubiquity.util.Constants.RENAME_ANNOTATION;

/**
 * @author François LAROCHE
 *
 */
final class PropertyRetrieverVisitor extends ClassVisitor {

    private static final List<String> COLLECTIONS;
    static {
        COLLECTIONS = new ArrayList<String>();
        COLLECTIONS.add("()Ljava/util/List;");
        COLLECTIONS.add("()Ljava/util/Set;");
        COLLECTIONS.add("()Ljava/util/Map;");
        COLLECTIONS.add("(Ljava/util/List;)V");
        COLLECTIONS.add("(Ljava/util/Set;)V");
        COLLECTIONS.add("(Ljava/util/Map;)V");

    }

	private final Map<String, Property> properties;
    private PropertyRetrieverVisitor parent;

	public PropertyRetrieverVisitor() {
		super(ASM_LEVEL);
		this.properties = new HashMap<String, Property>();
	}

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if(!"java/lang/Object".equals(superName)) {
            try {
                this.parent = new PropertyRetrieverVisitor();
                ClassReader r = new ClassReader(superName);
                r.accept(this.parent, 0);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to find class " + superName, e);
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if(isGetterOrSetter(name)) {
			Property property = getProperty(getPropertyName(name));
			char start = name.charAt(0);
			if(start == 's') {
                property.setSetter(name);
                if(COLLECTIONS.contains(desc) && signature != null) {
                    property.setTypeSetter(parseParameterFromDesc(desc));
                    property.setGenericSetter(parsegenericsFronSignature(signature));
                }
                else {
                    property.setTypeSetter(parseParameterFromDesc(desc));
                }
			}
			else {
                property.setGetter(name);
                if(COLLECTIONS.contains(desc) && signature != null) {
                    property.setTypeGetter(parseReturnTypeFromDesc(desc));
                    property.setGenericGetter(parsegenericsFronSignature(signature));
                }
                else {
                    property.setTypeGetter(parseReturnTypeFromDesc(desc));
                }
			}
			return new MethodReader(property);
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	private Property getProperty(String name) {
		if(!this.properties.containsKey(name)) {
			this.properties.put(name, new Property(name));
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

	private final class MethodReader extends MethodVisitor {
		
		private final Property property;
//        private static final String UBIQUITY_ANNOTATION = "Lorg/ubiquity/annotation/";
		
		private MethodReader(Property property) {
			super(ASM_LEVEL);
			this.property = property;
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			if(visible) {
                if(Constants.IGNORE_ANNOTATION.equals(desc)) {
                    this.property.getAnnotations().add(desc);
                }
                if(Constants.RENAME_ANNOTATION.equals(desc)) {
                    return new RenameAnotationVisitor(this.property);
                }

                if(Constants.RENAMES_ANNOTATION.equals(desc)) {
                    return new AnnotationVisitor(ASM_LEVEL) {
                        @Override
                        public AnnotationVisitor visitAnnotation(String name, String desc) {
                            return new RenameAnotationVisitor(property);
                        }
                    };
                }
			}
			return super.visitAnnotation(desc, visible);
		}

	}

    /**
     * Visitor for the rename annotations {@link org.ubiquity.annotation.CopyRename}
     */
    static class RenameAnotationVisitor extends AnnotationVisitor {
        private final Property property;

        private String targetClass;
        private String targetName;

        public RenameAnotationVisitor(Property property) {
            super(ASM_LEVEL);
            this.property = property;
        }

        @Override
        public void visit(String name, Object value) {
            if("propertyName".equals(name)) {
                this.targetName = (String) value;
            }
            else if("targetClass".equals(name)) {
                Type t = (Type) value;
                this.targetClass = t.getDescriptor();
            }
        }

        @Override
        public void visitEnd() {
            this.property.getAnnotations().add(RENAME_ANNOTATION + ':' + this.targetName + ':' + this.targetClass);
        }
    }
	
	@Override public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PropertyRetrieverVisitor {");
        Map<String, Property> props = getProperties();
		for(String key : props.keySet()) {
			builder.append('\n')
			.append("\t- Property ")
			.append(key)
			.append(" : ")
			.append(props.get(key))
			;
		}
		builder.append('}');
		return builder.toString();
	}

	public Map<String, Property> getProperties() {
        Map<String, Property> mergedProperties = new HashMap<String, Property>();
        if(this.parent != null) {
            mergedProperties.putAll(parent.getProperties());
        }
        mergedProperties.putAll(this.properties);
		return mergedProperties;
	}

    private static String parseType(String value) {
        if(value.startsWith("L")) {
            return value.substring(1, value.indexOf(';'));
        }
        else {
            return value;
        }
    }

    private static String parseParameterFromDesc(String desc) {
        String value = desc.substring(desc.indexOf('(') + 1, desc.indexOf(')'));
        return parseType(value);
    }


    private static String parseReturnTypeFromDesc(String desc) {
        return parseType(desc.substring(desc.indexOf(')') + 1));
    }

    private String parsegenericsFronSignature(String signature) {
        if(signature == null || !signature.contains("<") || !signature.contains(">")) {
            return null;
        }
        String generics = signature.substring(signature.indexOf('<') + 1, signature.indexOf('>'));
        return parseGenericsInternal(generics);
    }

    private String parseGenericsInternal(String generics) {
        if(generics.length() == 1) {
            return generics;
        }
        if(generics.charAt(0) == 'L' && generics.indexOf(';') == generics.length() - 1) {
            return generics;
        }
        int index = generics.indexOf(';');
        // If no complex objects, return the last simple one
        if(index == -1) {
            return generics.substring(generics.length() - 1);
        }
        return parseGenericsInternal(generics.substring(index + 1));
    }
}
