package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleStatusNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        RoleStatusNullException exception = new RoleStatusNullException();
        assertNotNull(exception);
        assertEquals("error.role.status.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(RoleStatusNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(RoleStatusNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        RoleStatusNullException exception = new RoleStatusNullException();
        assertEquals(1104, exception.getCode());
        assertEquals("error.role.status.null", exception.getMessageKey());
    }
}
