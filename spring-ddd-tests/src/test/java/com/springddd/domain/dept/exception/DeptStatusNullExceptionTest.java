package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptStatusNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        DeptStatusNullException exception = new DeptStatusNullException();
        assertNotNull(exception);
        assertEquals("Dept status cannot be null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(DeptStatusNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(DeptStatusNullException.class));
    }
}
