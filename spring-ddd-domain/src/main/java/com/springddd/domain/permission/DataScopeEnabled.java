package com.springddd.domain.permission;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScopeEnabled {

    String entityCode() default "";
}
