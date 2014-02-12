package org.ubiquity.mirror.objects;

/**
 */
public class SimpleAnnotatedObject {

    private String annotatedField;

    @BasicAnnotation("testMe")
    public String getAnnotatedField() {
        return annotatedField;
    }

    public void setAnnotatedField(String annotatedField) {
        this.annotatedField = annotatedField;
    }
}
