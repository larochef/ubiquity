package org.ubiquity.bytecode;

import org.ubiquity.util.SimpleCopier;

/**
 * Created with IntelliJ IDEA.
 * User: famille
 * Date: 13/04/12
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public class CodeTest extends SimpleCopier<SimpleTestClass, SimpleTestClass> {

    @Override
    public SimpleTestClass map(SimpleTestClass element) {
        SimpleTestClass result = new SimpleTestClass();
        result.setProperty1(element.getProperty1());
        return result;
    }
}
