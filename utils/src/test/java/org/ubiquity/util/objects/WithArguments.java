package org.ubiquity.util.objects;

/**
 *
 */
public @interface WithArguments {

    String stringValue();

    WithArgumentEnum enumValue() default WithArgumentEnum.NONE;
}
