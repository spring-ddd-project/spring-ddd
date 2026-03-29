package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SortOrderNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        SortOrderNullException exception = new SortOrderNullException();
        assertNotNull(exception);
        assertEquals("Sort order cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(SortOrderNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(SortOrderNullException.class));
    }
}
