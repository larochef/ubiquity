package org.ubiquity.mirror.objects;

/**
 * Created by francois on 12/02/14.
 */
public class ComplexAnnotatedObject {

    private String field1;
    private String field2;

    @NativeAnnotation(byteValue = 1, charValue = 2, doubleValue = 3, floatValue = 4,
            intValue = 5, longValue = 6, shortValue = 7, booleanValue = true)
    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

//    @EnumAnnotation(TestEnum.VALUE1)
    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}
