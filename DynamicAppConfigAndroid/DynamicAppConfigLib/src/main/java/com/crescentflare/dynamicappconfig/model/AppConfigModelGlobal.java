package com.crescentflare.dynamicappconfig.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Library model: global annotation
 * Model fields marked with this annotation are used as global settings, apart from the configurations
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AppConfigModelGlobal {
}
