package com.crescentflare.dynamicappconfig.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Library model: category annotation
 * Recommended if there are many settings, use with model members to group settings into categories
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AppConfigModelCategory
{
    String value() default "";
}
