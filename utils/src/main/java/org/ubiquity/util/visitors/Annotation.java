package org.ubiquity.util.visitors;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Date: 14/04/13
 *
 * @author François LAROCHE
 */
public class Annotation {

    private String clazz;
    private Map<String, AnnotationProperty> properties;
    boolean visible;

    public Annotation() {
        this.properties = Maps.newHashMap();
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Map<String, AnnotationProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, AnnotationProperty> properties) {
        this.properties = properties;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
