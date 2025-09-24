package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsernameExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        UsernameException exception = new UsernameException();
        assertNotNull(exception);
        assertEquals("error.user.name.null", exception.getMessage());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(UsernameException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(UsernameException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        UsernameException exception = new UsernameException();
        assertEquals(1000, exception.getCode());
        assertEquals("error.user.name.null", exception.getMessageKey());
    }
}
