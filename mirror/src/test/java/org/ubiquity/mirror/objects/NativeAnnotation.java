package org.ubiquity.mirror.objects;

/**
 * Created by francois on 12/02/14.
 */
public @interface NativeAnnotation {

    int intValue() default 0;
    boolean booleanValue() default false;
    short shortValue() default 0;
    long longValue() default 0;
    byte byteValue() default 0;
    char charValue() default 0;
    float floatValue() default 0F;
    double doubleValue() default 0D;
}
