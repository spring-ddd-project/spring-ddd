package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import static org.junit.jupiter.api.Assertions.*;

class IdGenerateAnnotationTest {

    @Test
    void idGenerateAnnotation_ShouldHaveCorrectRetentionPolicy() {
        // Given
        Retention retention = IdGenerate.class.getAnnotation(Retention.class);

        // Then
        assertNotNull(retention, "IdGenerate should have Retention annotation");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Retention should be RUNTIME");
    }

    @Test
    void idGenerateAnnotation_ShouldHaveCorrectTarget() {
        // Given
        Target target = IdGenerate.class.getAnnotation(Target.class);

        // Then
        assertNotNull(target, "IdGenerate should have Target annotation");
        assertEquals(1, target.value().length, "IdGenerate should have exactly one target");
        assertEquals(ElementType.FIELD, target.value()[0], "Target should be FIELD");
    }

    @Test
    void idGenerateAnnotation_ShouldBePresent() {
        // Verify the annotation class exists and can be loaded
        assertNotNull(IdGenerate.class, "IdGenerate annotation class should exist");
    }

    @Test
    void idGenerateAnnotation_ShouldBeAnnotatable() throws NoSuchFieldException {
        // Test that we can apply the annotation to a field
        class TestEntity {
            @IdGenerate
            private Long id;
        }

        // When
        IdGenerate annotation = TestEntity.class.getDeclaredField("id").getAnnotation(IdGenerate.class);

        // Then
        assertNotNull(annotation, "Annotation should be present on field");
    }
}
