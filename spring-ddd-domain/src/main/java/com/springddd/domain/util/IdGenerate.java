package com.springddd.domain.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdGenerate {

    IdGenerateStrategy strategy() default IdGenerateStrategy.ID_TEMP;

    String key() default "";
}
