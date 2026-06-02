package com.springddd.domain.leaf.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        StepNullException exception = new StepNullException();
        assertNotNull(exception);
        assertEquals("error.leaf.step.null", exception.getMessage());
    }

    @Test
    void shouldExtendLeafAllocException() {
        assertTrue(LeafAllocException.class.isAssignableFrom(StepNullException.class));
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(StepNullException.class));
    }

    @Test
    void shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(StepNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        StepNullException exception = new StepNullException();
        assertEquals(1603, exception.getCode());
    }
}
