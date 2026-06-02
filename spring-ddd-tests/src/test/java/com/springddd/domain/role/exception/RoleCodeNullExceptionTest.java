package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleCodeNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        RoleCodeNullException exception = new RoleCodeNullException();
        assertNotNull(exception);
        assertEquals("error.role.code.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(RoleCodeNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(RoleCodeNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        RoleCodeNullException exception = new RoleCodeNullException();
        assertEquals(1100, exception.getCode());
        assertEquals("error.role.code.null", exception.getMessageKey());
    }
}
