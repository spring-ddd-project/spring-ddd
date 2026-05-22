package com.springddd.domain.permission;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermissionEntity {

    String entityCode() default "";

    String name() default "";
}
