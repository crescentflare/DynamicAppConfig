package com.crescentflare.dynamicappconfig.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Library model: sorting annotation
 * Use to force a sorting order for the editing screen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AppConfigModelSort {
    int value() default 0;
}
