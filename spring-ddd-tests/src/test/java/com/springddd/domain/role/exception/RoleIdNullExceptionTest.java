package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleIdNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        RoleIdNullException exception = new RoleIdNullException();
        assertNotNull(exception);
        assertEquals("error.role.id.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(RoleIdNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(RoleIdNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        RoleIdNullException exception = new RoleIdNullException();
        assertEquals(1103, exception.getCode());
        assertEquals("error.role.id.null", exception.getMessageKey());
    }
}
