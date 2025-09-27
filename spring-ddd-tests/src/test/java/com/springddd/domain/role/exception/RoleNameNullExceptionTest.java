package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleNameNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        RoleNameNullException exception = new RoleNameNullException();
        assertNotNull(exception);
        assertEquals("error.role.name.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(RoleNameNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(RoleNameNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        RoleNameNullException exception = new RoleNameNullException();
        assertEquals(1101, exception.getCode());
        assertEquals("error.role.name.null", exception.getMessageKey());
    }
}
