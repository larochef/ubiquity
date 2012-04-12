/**
 * 
 */
package org.ubiquity.bytecode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author François LAROCHE
 *
 */
public class Property {

	private String name;
	private String getter;
	private String setter;
	
	private List<String> annotations;
	
	public Property() {
		this.annotations = new ArrayList<String>();
	}
	
	public boolean isReadable() {
		return getter != null;
	}
	
	public boolean isWritable() {
		return this.setter != null;
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

	@Override
	public String toString() {
		return "Property [name=" + String.valueOf(name) + ", getter=" + String.valueOf(getter) + ", setter="
				+ String.valueOf(setter) + ", annotations=" + annotations + "]";
	}
	
	
	
}
