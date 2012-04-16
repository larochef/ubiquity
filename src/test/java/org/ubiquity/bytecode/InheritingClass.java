package org.ubiquity.bytecode;

/**
 * Date: 16/04/12
 *
 * @author François LAROCHE
 */
public class InheritingClass extends SimpleTestClass {

    private String myProperty;

    public String getMyProperty() {
        return myProperty;
    }

    public void setMyProperty(String myProperty) {
        this.myProperty = myProperty;
    }
}
