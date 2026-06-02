package com.springddd.domain.leaf.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaxIdNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MaxIdNullException exception = new MaxIdNullException();
        assertNotNull(exception);
        assertEquals("error.leaf.maxId.null", exception.getMessage());
    }

    @Test
    void shouldExtendLeafAllocException() {
        assertTrue(LeafAllocException.class.isAssignableFrom(MaxIdNullException.class));
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MaxIdNullException.class));
    }

    @Test
    void shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MaxIdNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MaxIdNullException exception = new MaxIdNullException();
        assertEquals(1602, exception.getCode());
    }
}
