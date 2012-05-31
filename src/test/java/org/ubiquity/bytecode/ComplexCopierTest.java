package org.ubiquity.bytecode;

/**
 * TODO : explain.me
 * Date: 29/05/12
 * Time: 15:42
 *
 * @author françois LAROCHE
 */
public class ComplexCopierTest extends SimpleCopier<InheritingClass, InheritingClass> {

    protected ComplexCopierTest(CopyContext context) {
        super(context);
    }

    @Override
    public void copy(InheritingClass source, InheritingClass destination) {
        if(destination.getParent() != null) {
            if(destination.getParent().getParent() != null) {
                destination.getParent().setProperty1(source.getProperty1());
            }
        }
        if(source.getParent() != null && source.getParent().getParent() != null) {
            destination.setProperty1(source.getParent().getParent().getProperty1());
        }
    }

    @Override
    protected InheritingClass newInstance() {
        return new InheritingClass();
    }

    @Override
    protected InheritingClass[] newArray(int capacity) {
        return new InheritingClass[capacity];
    }
}
