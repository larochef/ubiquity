/**
 * 
 */
package org.ubiquity.util.visitors;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * @author François LAROCHE
 *
 */
public final class BytecodeProperty {

	final private String name;
	private String getter;
	private String setter;
    private String typeGetter;
    private String typeSetter;
    private Map<String, String> genericGetter;
    private Map<String, String> genericSetter;

	private List<Annotation> annotations;
	
	public BytecodeProperty(String name) {
		this.annotations = Lists.newArrayList();
        this.name = name;
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

	public List<Annotation> getAnnotations() {
		return annotations;
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

    public Map<String, String> getGenericGetter() {
        return genericGetter;
    }

    public void setGenericGetter(Map<String, String> genericGetter) {
        this.genericGetter = genericGetter;
    }

    public Map<String, String> getGenericSetter() {
        return genericSetter;
    }

    public void setGenericSetter(Map<String, String> genericSetter) {
        this.genericSetter = genericSetter;
    }

    public String getDefaultGenericsGetterValue() {
        return getDefaultValue(this.genericGetter);
    }

    public String getDefaultGenericsSetterValue() {
        return getDefaultValue(this.genericSetter);
    }

    private String getDefaultValue(Map<String, String> genericsMap) {
        if(genericsMap == null || genericsMap.isEmpty()) {
            return null;
        }
        if(genericsMap.containsKey("T")) {
            return genericsMap.get("T");
        }
        if(genericsMap.containsKey("V")) {
            return genericsMap.get("V");
        }
        return genericsMap.values().iterator().next();
    }

    @Override
	public String toString() {
		return "BytecodeProperty [name=" + String.valueOf(name) + ", getter=" + String.valueOf(getter) + ", setter="
				+ String.valueOf(setter) + ", annotations=" + annotations + "]";
	}
}
