package org.ubiquity.bytecode;

import org.ubiquity.annotation.CopyRename;
import org.ubiquity.annotation.CopyRenames;

/**
 * Date: 25/05/12
 *
 * @author François LAROCHE
 */
public class AnnotatedClass {

    private String property1;
    private String property2;
    private String property3;

    @CopyRename(targetClass = SimpleTestClass.class, propertyName = "property3")
    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    @CopyRename(propertyName = "test")
    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    @CopyRenames(configurations = {@CopyRename(propertyName = "test"),@CopyRename(propertyName = "property3", targetClass = SimpleTestClass.class)})
    public String getProperty3() {
        return property3;
    }

    public void setProperty3(String property3) {
        this.property3 = property3;
    }
}
