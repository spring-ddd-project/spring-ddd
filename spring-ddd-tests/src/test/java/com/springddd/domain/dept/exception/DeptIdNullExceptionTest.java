package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptIdNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DeptIdNullException exception = new DeptIdNullException();
        assertNotNull(exception);
        assertEquals("error.dept.id.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DeptIdNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DeptIdNullException.class));
    }
}
