/**
 * 
 */
package org.ubiquity.bytecode;

import org.ubiquity.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author François LAROCHE
 *
 */
public class Property {

    private static final List<String> SIMPLE_PROPERTIES = Arrays.asList("Z", "I", "F", "J", "D", "C", "S");

	private String name;
	private String getter;
	private String setter;
    private String typeGetter;
    private String typeSetter;
    private String typeField;
	
	private List<String> annotations;
	
	public Property(String type) {
		this.annotations = new ArrayList<String>();
	}
	
	public boolean isReadable() {
		return getter != null && !this.isIgnored();
	}
	
	public boolean isWritable() {
		return this.setter != null && !this.isIgnored();
	}

	public String getGetter() {
		return getter;
	}

	public void setGetter(String getter) {
		this.getter = getter;
	}

	public String getSetter() {
		return setter;
	}

	public void setSetter(String setter) {
		this.setter = setter;
	}

	public String getName() {
		return name;
	}

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getTypeGetter() {
        return typeGetter;
    }

    public void setTypeGetter(String typeGetter) {
        this.typeGetter = typeGetter;
    }

    public String getTypeSetter() {
        return typeSetter;
    }

    public void setTypeSetter(String typeSetter) {
        this.typeSetter = typeSetter;
    }

    public String getTypeField() {
        return typeField;
    }

    public void setTypeField(String typeField) {
        this.typeField = typeField;
    }

    private boolean isIgnored() {
        return this.annotations != null && this.annotations.contains(Constants.IGNORE_ANNOTATION);
    }

    public boolean isArray(PropertyType type) {
        String property = type == PropertyType.GETTER ? this.typeGetter : this.typeSetter;
        return this.name.charAt(0) == '[';
    }

    public boolean isSimpleType(PropertyType type) {
        String property = type == PropertyType.GETTER ? this.typeGetter : this.typeSetter;
        if(property.startsWith("[")) {
            property = property.substring(1);
        }
        return SIMPLE_PROPERTIES.contains(property);
    }

    @Override
	public String toString() {
		return "Property [name=" + String.valueOf(name) + ", getter=" + String.valueOf(getter) + ", setter="
				+ String.valueOf(setter) + ", annotations=" + annotations + "]";
	}

    public enum PropertyType {
        GETTER, SETTER
    }
	
	
}
