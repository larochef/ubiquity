package org.ubiquity.bytecode;

/**
 * TODO : explain.me
 * Date: 23/04/12
 * Time: 13:10
 *
 * @Author françois LAROCHE
 */
public class InheritingClass2 extends SimpleTestClass {

    private InheritingClass2 parent;

    public InheritingClass2 getParent() {
        return parent;
    }

    public void setParent(InheritingClass2 parent) {
        this.parent = parent;
    }
}
