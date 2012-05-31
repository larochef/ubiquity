package org.ubiquity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignores a property for copy.
 * This annotation can be used either on getter or on setter, with same effect.
 *
 * Date: 16/04/12
 *
 * @author François LAROCHE
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CopyIgnore {
}
