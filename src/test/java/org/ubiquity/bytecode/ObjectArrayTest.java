package org.ubiquity.bytecode;

/**
 * TODO : explain.me
 * Date: 24/04/12
 * Time: 16:05
 *
 * @Author françois LAROCHE
 */
public class ObjectArrayTest {
    private ObjectArrayTest[] objects;

    public ObjectArrayTest[] getObjects() {
        return objects;
    }

    public void setObjects(ObjectArrayTest[] objects) {
        this.objects = objects;
    }

    public static class ObjectArrayTest2 {
        ObjectArrayTest2[] objects;

        public ObjectArrayTest2[] getObjects() {
            return objects;
        }

        public void setObjects(ObjectArrayTest2[] objects) {
            this.objects = objects;
        }
    }
}
