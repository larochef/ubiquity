package org.ubiquity.mirror.objects;

/**
 * Created by francois on 12/02/14.
 */
public @interface EnumAnnotation {
    TestEnum value() default TestEnum.VALUE2;
}
