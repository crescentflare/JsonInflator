package com.crescentflare.jsoninflator.binder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Json inflator binder: annotation
 * Annotate fields to automatically assign with the InflatorAnnotationBinder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InflatableRef
{
    String value() default "";
}
