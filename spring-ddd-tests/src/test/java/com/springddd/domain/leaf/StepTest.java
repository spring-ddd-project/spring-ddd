package com.springddd.domain.leaf;

import com.springddd.domain.leaf.exception.StepInvalidException;
import com.springddd.domain.leaf.exception.StepNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepTest {

    @Test
    void shouldCreateStepWithValidValue() {
        Step step = new Step(100);
        assertEquals(100, step.value());
    }

    @Test
    void shouldThrowStepNullExceptionWhenValueIsNull() {
        assertThrows(StepNullException.class, () -> new Step(null));
    }

    @Test
    void shouldThrowStepInvalidExceptionWhenValueIsZero() {
        assertThrows(StepInvalidException.class, () -> new Step(0));
    }

    @Test
    void shouldThrowStepInvalidExceptionWhenValueIsNegative() {
        assertThrows(StepInvalidException.class, () -> new Step(-1));
    }

    @Test
    void shouldCreateStepWithValueOne() {
        Step step = new Step(1);
        assertEquals(1, step.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        Step step1 = new Step(100);
        Step step2 = new Step(100);
        assertEquals(step1, step2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        Step step1 = new Step(100);
        Step step2 = new Step(200);
        assertNotEquals(step1, step2);
    }

    @Test
    void hashCode_shouldBeConsistentForSameValue() {
        Step step1 = new Step(100);
        Step step2 = new Step(100);
        assertEquals(step1.hashCode(), step2.hashCode());
    }

    @Test
    void toString_shouldContainValue() {
        Step step = new Step(100);
        assertTrue(step.toString().contains("100"));
    }
}
