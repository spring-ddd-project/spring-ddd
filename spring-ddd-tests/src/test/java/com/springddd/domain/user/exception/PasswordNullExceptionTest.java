package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        PasswordNullException exception = new PasswordNullException();
        assertNotNull(exception);
        assertEquals("error.user.password.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(PasswordNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(PasswordNullException.class));
    }
}