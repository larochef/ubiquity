/**
 * 
 */
package org.ubiquity.bytecode;

import org.ubiquity.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author François LAROCHE
 *
 */
class  Property {

	private String name;
	private String getter;
	private String setter;
    private String typeGetter;
    private String typeSetter;
    private String genericGetter;
    private String genericSetter;

	private List<String> annotations;
	
	public Property() {
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

    private boolean isIgnored() {
        return this.annotations != null && this.annotations.contains(Constants.IGNORE_ANNOTATION);
    }

    public String getGenericGetter() {
        return genericGetter;
    }

    public void setGenericGetter(String genericGetter) {
        this.genericGetter = genericGetter;
    }

    public String getGenericSetter() {
        return genericSetter;
    }

    public void setGenericSetter(String genericSetter) {
        this.genericSetter = genericSetter;
    }

    @Override
	public String toString() {
		return "Property [name=" + String.valueOf(name) + ", getter=" + String.valueOf(getter) + ", setter="
				+ String.valueOf(setter) + ", annotations=" + annotations + "]";
	}
}
