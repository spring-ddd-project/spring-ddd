package com.springddd.domain.leaf.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepInvalidExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        StepInvalidException exception = new StepInvalidException();
        assertNotNull(exception);
        assertEquals("error.leaf.step.invalid", exception.getMessage());
    }

    @Test
    void shouldExtendLeafAllocException() {
        assertTrue(LeafAllocException.class.isAssignableFrom(StepInvalidException.class));
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(StepInvalidException.class));
    }

    @Test
    void shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(StepInvalidException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        StepInvalidException exception = new StepInvalidException();
        assertEquals(1604, exception.getCode());
    }
}
