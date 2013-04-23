package org.ubiquity.util;

import org.ubiquity.Copier;

import java.lang.reflect.InvocationTargetException;

/**
 * Class loader allowing to define classes.
 * This ClassLoader will be used to load the copiers generated classes.
 * <br />
 * Date: 08/06/12
 * Time: 13:01
 *
 * @author françois LAROCHE
 */
final class UbiquityClassLoader extends ClassLoader {

    Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }

    <T,U> Copier<T,U> registerCopier(byte[] bytecode, String className, CopyContext ctx) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> resultClass = this.defineClass(className.replace('/', '.'), bytecode);
        @SuppressWarnings("unchecked")
        Copier<T,U> instance =  (Copier<T,U>) resultClass.getConstructor(CopyContext.class).newInstance(ctx);
        return instance;
    }

    <T> ClassHandler<T> registerHandler(Class<T> src, byte[] byteCode) {
        return null;
    }
}
