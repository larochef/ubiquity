/**
 * 
 */
package org.ubiquity.bytecode;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.ubiquity.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.ubiquity.util.Constants.ASM_LEVEL;
import static org.ubiquity.util.Constants.RENAME_ANNOTATION;

/**
 * @author François LAROCHE
 *
 */
final class PropertyRetrieverVisitor extends ClassVisitor {

    private static final List<String> LETTERS;
    private static final List<String> SINGLE_LETTER;
    private static final List<String> TWO_LETTERS;


    static {
        SINGLE_LETTER = Arrays.asList("T");
        TWO_LETTERS = Arrays.asList("K", "V");
        LETTERS = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    }

	private final Map<String, Property> properties;
    private PropertyRetrieverVisitor parent;

	public PropertyRetrieverVisitor() {
		super(ASM_LEVEL);
		this.properties = new HashMap<String, Property>();
	}

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if(!"java/lang/Object".equals(superName) && superName != null) {
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
                if(signature != null) {
                    property.setTypeSetter(parseParameterFromDesc(desc));
                    property.setGenericSetter(parseGenericsFromSignature(signature));
                }
                else {
                    property.setTypeSetter(parseParameterFromDesc(desc));
                }
			}
			else {
                property.setGetter(name);
                if(signature != null) {
                    property.setTypeGetter(parseReturnTypeFromDesc(desc));
                    property.setGenericGetter(parseGenericsFromSignature(signature));
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
        String upperName;
        if(functionName.startsWith("is")) {
            upperName = functionName.substring(2);
        }
        else {
            upperName = functionName.substring(3);
        }
        if(upperName.length() == 1) {
            return upperName.toLowerCase();
        }
        return upperName.substring(0,1).toLowerCase() + upperName.substring(1);
	}

	private static boolean isGetterOrSetter(String name) {
		if(name.length() < 4) {
			return false;
		}
		if(!name.startsWith("get") && !name.startsWith("set") && !name.startsWith("is")) {
			return false;
		}
		char propertyStart = name.charAt(name.startsWith("is") ? 2 : 3);
		return propertyStart >= 'A' && propertyStart <= 'Z';
	}

	private final class MethodReader extends MethodVisitor {
		
		private final Property property;

		private MethodReader(Property property) {
			super(ASM_LEVEL);
			this.property = property;
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if(Constants.IGNORE_ANNOTATION.equals(desc)) {
                this.property.getAnnotations().add(desc);
            }
            if(Constants.RENAME_ANNOTATION.equals(desc)) {
                return new RenameAnotationVisitor(this.property);
            }

            if(Constants.RENAMES_ANNOTATION.equals(desc)) {
                return new AnnotationVisitor(ASM_LEVEL) {
                    @Override
                    public AnnotationVisitor visitArray(String name) {
                        return this;
                    }
                    @Override
                    public AnnotationVisitor visitAnnotation(String name, String desc) {
                        return new RenameAnotationVisitor(property);
                    }
                };
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
                if(targetName.contains(".")) {
                    throw new IllegalArgumentException("Deep renaming not supporting yet, work in progress.");
                }
            }
            else if("targetClass".equals(name)) {
                Type t = (Type) value;
                this.targetClass = t.getDescriptor();
            }
        }

        @Override
        public AnnotationVisitor visitArray(String name) {
            return this;
        }

        @Override
        public void visitEnd() {
            if("Ljava/lang/Object;".equals(targetClass) || null == targetClass) {
                targetClass = "*";
            }
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

    private Map<String, String> parseGenericsFromSignature(String signature) {
        Map<String, String> result = new HashMap<String, String>();
        // Clean signature to only get only the wanted type
        if(signature == null || !signature.contains("<") || !signature.contains(">")) {
            return null;
        }
        String typesOnly;
        if(signature.startsWith("()")) {
            typesOnly = signature.substring(2);
        }
        else if (signature.startsWith("(") && signature.endsWith(")V")) {
            typesOnly = signature.substring(1, signature.length() - 2);
        }
        else {
            typesOnly = signature;
        }

        String allGenerics = typesOnly.substring(typesOnly.indexOf('<') + 1, typesOnly.lastIndexOf('>'));
        List<String> genericTypes = new ArrayList<String>();
        while(allGenerics.length() > 0) {
            String gen = getFirstType(allGenerics);
            if(gen == null) {
                allGenerics = "";
                continue;
            }

            genericTypes.add(gen);
            if(gen.length() != allGenerics.length()) {
                int index = allGenerics.indexOf(gen);
                allGenerics = allGenerics.substring(index + gen.length(), allGenerics.length());
            }
            else {
                allGenerics = "";
            }
            if(">;".equals(allGenerics)) {
                allGenerics = "";
            }
        }

        Iterator<String> letters;
        if(genericTypes.size() == 1) {
            letters = SINGLE_LETTER.iterator();
        }
        else if (genericTypes.size() == 2) {
            letters = TWO_LETTERS.iterator();
        }
        else {
            letters = LETTERS.iterator();
        }
        for(String gen : genericTypes) {
            String letter = letters.next();
            if(gen.contains(":")) {
                String[] values = Constants.SEPARATOR_PATTERN.split(gen);
                letter = values[0];
                result.put(letter, values[1]);
            }
            else {
                result.put(letter, gen);
            }
        }
        return result;
    }

    private String getFirstType(String types) {
        int begin = types.indexOf("L");
        if(begin == -1) {
            return null;
        }
        int indexGen = types.indexOf('<', begin);
        int indexType = types.indexOf(';', begin);
        if(indexGen == -1) {
            indexGen = Integer.MAX_VALUE;
        }
        if(indexType == -1) {
            indexType = types.length() - 1;
        }
        if(indexType < indexGen) {
            return types.substring(begin, indexType + 1);
        }
        return types.substring(begin, getClosingIndex(types, indexGen) + 2); // Add the ";" after the ">"
    }

    private int getClosingIndex(String str, int openIndex) {
        if(str  == null || str.length() < openIndex || str.charAt(openIndex) != '<') {
            return -1;
        }
        int nbOpen = 0;
        for(int i = openIndex; i < str.length(); i++) {
            if(str.charAt(i) == '<') {
                nbOpen ++;
            }
            if(str.charAt(i) == '>') {
                nbOpen --;
            }
            if(nbOpen == 0) {
                return i;
            }
        }
        return -1;
    }
}
