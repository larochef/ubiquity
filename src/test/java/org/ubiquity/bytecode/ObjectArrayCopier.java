package org.ubiquity.bytecode;

/**
 * TODO : explain.me
 * Date: 24/04/12
 * Time: 18:08
 *
 * @Author françois LAROCHE
 */
public class ObjectArrayCopier extends SimpleCopier<ObjectArrayTest, ObjectArrayTest.ObjectArrayTest2> {
    public ObjectArrayCopier(CopyContext context) {
        super(context);
    }

    @Override
    protected ObjectArrayTest.ObjectArrayTest2 newInstance() {
        return new ObjectArrayTest.ObjectArrayTest2();
    }

    @Override
    protected ObjectArrayTest.ObjectArrayTest2[] newArray(int capacity) {
        return new ObjectArrayTest.ObjectArrayTest2[capacity];
    }

    @Override
    public void copy(ObjectArrayTest source, ObjectArrayTest.ObjectArrayTest2 destination){
        org.ubiquity.Copier<ObjectArrayTest, ObjectArrayTest.ObjectArrayTest2> copier =
                context.getCopier(ObjectArrayTest.class, ObjectArrayTest.ObjectArrayTest2.class);
        destination.setObjects(copier.map(source.getObjects(), destination.getObjects()));
    }
}
