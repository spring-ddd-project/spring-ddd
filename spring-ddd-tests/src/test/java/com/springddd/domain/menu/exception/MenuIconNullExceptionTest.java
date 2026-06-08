package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuIconNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MenuIconNullException exception = new MenuIconNullException();
        assertNotNull(exception);
        assertEquals("error.menu.icon.null", exception.getMessageKey());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MenuIconNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MenuIconNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MenuIconNullException exception = new MenuIconNullException();
        assertEquals(1203, exception.getCode());
    }
}
