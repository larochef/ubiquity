package org.ubiquity.bytecode;

/**
 * TODO : explain.me
 * Date: 24/04/12
 * Time: 16:05
 *
 * @Author françois LAROCHE
 */
public class ObjectArrayTest {
    private InheritingClass[] objects;

    public InheritingClass[] getObjects() {
        return objects;
    }

    public void setObjects(InheritingClass[] objects) {
        this.objects = objects;
    }

    public static class ObjectArrayTest2 {
        InheritingClass2[] objects;

        public InheritingClass2[] getObjects() {
            return objects;
        }

        public void setObjects(InheritingClass2[] objects) {
            this.objects = objects;
        }
    }
}
