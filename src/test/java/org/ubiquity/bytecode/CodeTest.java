package org.ubiquity.bytecode;

/**
 * Created with IntelliJ IDEA.
 * User: famille
 * Date: 13/04/12
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public class CodeTest extends SimpleCopier<SimpleTestClass, SimpleTestClass> {

    public CodeTest(CopyContext context) {
        super(context);
    }

    @Override
    public void copy(SimpleTestClass src, SimpleTestClass target) {
        target.setProperty1(src.getProperty1());
    }

    @Override
    protected SimpleTestClass newInstance() {
        return new SimpleTestClass();
    }
}
