package org.ubiquity.util;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public class SimpleClassLoader extends ClassLoader {

    public <T> Class<T> defineClass(String name, byte[] content) {
        return this.defineClass(name, content);
    }

}
