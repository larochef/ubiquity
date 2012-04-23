package org.ubiquity.bytecode;

import org.ubiquity.Copier;

/**
 * TODO : explain.me
 * Date: 23/04/12
 * Time: 13:18
 *
 * @Author françois LAROCHE
 */
public class CopierTest extends SimpleCopier<InheritingClass, InheritingClass2> {

    protected CopierTest(CopyContext context) {
        super(context);
    }

    @Override
    protected InheritingClass2 newInstance() {
        return new InheritingClass2();
    }

    @Override
    protected InheritingClass2[] newArray(int capacity) {
        return new InheritingClass2[capacity];
    }

    @Override
    public void copy(InheritingClass source, InheritingClass2 destination) {
        destination.setProperty1(source.getProperty1());
        if(source.getParent() == null) {
            destination.setParent(null);
        }
        else {
            Copier<InheritingClass, InheritingClass2> copier = this.context.getCopier(InheritingClass.class, InheritingClass2.class);
            if(destination.getParent() == null) {
                destination.setParent(copier.map(source.getParent()));
            }
            else {
                copier.copy(source.getParent(), destination.getParent());
            }
        }
    }
}
