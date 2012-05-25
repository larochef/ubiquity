package org.ubiquity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation allowing multiple configuration for the same property.
 *
 * Date: 25/05/12
 *
 * @author François LAROCHE
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CopyRenames {

    /**
     * The different property configurations for the target property
     *
     * @return the different property configurations for the target property
     */
    public CopyRename[] configurations();

}
