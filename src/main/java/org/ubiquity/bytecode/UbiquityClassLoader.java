package org.ubiquity.bytecode;

/**
 * TODO : explain.me
 * Date: 08/06/12
 * Time: 13:01
 *
 * @author françois LAROCHE
 */
class UbiquityClassLoader extends ClassLoader {

    public String getFinalName(String name) {
        String finalName = name;
        int i = 0;
        while(this.findLoadedClass(finalName) != null) {
            finalName = name + i;
            i++;
        }
        return finalName;
    }

    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
