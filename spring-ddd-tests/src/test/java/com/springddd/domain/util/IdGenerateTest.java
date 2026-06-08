package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class IdGenerateTest {

    @Test
    void shouldHaveIdGenerateAnnotation() {
        Class<IdGenerate> annotationType = IdGenerate.class;
        assertNotNull(annotationType);
    }

    @Test
    void shouldHaveFieldTarget() {
        Target target = IdGenerate.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(ElementType.FIELD, target.value()[0]);
    }

    @Test
    void shouldHaveRuntimeRetention() {
        Retention retention = IdGenerate.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void shouldBeAnnotation() {
        Annotation annotation = IdGenerate.class.getAnnotation(IdGenerate.class);
        assertNull(annotation);
    }

    @Test
    void shouldHavePublicInterface() {
        assertTrue(IdGenerate.class.isAnnotation());
        assertEquals("IdGenerate", IdGenerate.class.getSimpleName());
    }

    @Test
    void annotationClassShouldHaveCorrectPackage() {
        assertEquals("com.springddd.domain.util", IdGenerate.class.getPackageName());
    }

    @Test
    void shouldBeRetainedAtRuntime() {
        Retention retention = IdGenerate.class.getAnnotation(Retention.class);
        assertNotNull(retention);
    }

    @Test
    void shouldOnlyTargetFields() {
        Target target = IdGenerate.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertArrayEquals(new ElementType[]{ElementType.FIELD}, target.value());
    }
}
