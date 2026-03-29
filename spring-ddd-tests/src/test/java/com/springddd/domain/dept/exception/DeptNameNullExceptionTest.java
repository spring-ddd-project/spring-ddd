package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptNameNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DeptNameNullException exception = new DeptNameNullException();
        assertNotNull(exception);
        assertEquals("Dept name cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DeptNameNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DeptNameNullException.class));
    }
}
