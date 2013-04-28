package org.ubiquity.util.objects;

import java.util.List;

/**
 *
 */
public class MyObject {

    private String property1;
    private int property2;
    private List<String> property3;

    @WithArguments(enumValue = WithArgumentEnum.VALUE1, stringValue = "toto")
    @WithSimpleArray({"value1", "value2"})
    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    @Simple
    public int getProperty2() {
        return property2;
    }

    public void setProperty2(int property2) {
        this.property2 = property2;
    }

    public List<String> getProperty3() {
        return property3;
    }

    public void setProperty3(List<String> property3) {
        this.property3 = property3;
    }

    public void doTheMagic() {}
}
