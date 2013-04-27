package org.ubiquity.bytecode;

/**
 * Date: 16/06/12
 *
 * @author François LAROCHE
 */
interface ClassHandler<T> {

    Object getProperty(T object, String propertyame);

    void setProperty(T object, String propertyName, Object value);

    Object callFunction(T object, String functionName, Object... parameters);

    void callProcedure(T object, String functionName, Object... parameters);
}
