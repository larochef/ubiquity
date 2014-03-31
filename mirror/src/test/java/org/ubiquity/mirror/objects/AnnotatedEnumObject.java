package org.ubiquity.mirror.objects;

/**
 * Created by francois on 31/03/2014.
 */
public class AnnotatedEnumObject {

    private String property1;

    @EnumAnnotation(TestEnum.VALUE1)
    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }
}
