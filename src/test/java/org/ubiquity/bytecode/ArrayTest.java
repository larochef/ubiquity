package org.ubiquity.bytecode;

/**
 * TODO : explain.me
 * Date: 24/04/12
 * Time: 12:07
 *
 * @Author françois LAROCHE
 */
public class ArrayTest extends SimpleCopier<ArrayClass, ArrayClass> {

    protected ArrayTest(CopyContext context) {
        super(context);
    }

    @Override
    protected ArrayClass newInstance() {
        return new ArrayClass();
    }

    @Override
    protected ArrayClass[] newArray(int capacity) {
        return new ArrayClass[capacity];
    }

    @Override
    public void copy(ArrayClass source, ArrayClass destination) {
        if(source.getIntArray() == null) {
            destination.setIntArray(null);
        }
        else {
            int[] arraySrc = source.getIntArray();
            int [] array = new int[arraySrc.length];
            destination.setIntArray(array);
            for(int i=0; i < array.length; i++) {
                array[i] = arraySrc[i];
            }
        }
    }
}
