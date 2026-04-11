package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuIdNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MenuIdNullException exception = new MenuIdNullException();
        assertNotNull(exception);
        assertEquals("error.menu.id.null", exception.getMessageKey());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MenuIdNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MenuIdNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MenuIdNullException exception = new MenuIdNullException();
        assertEquals(1205, exception.getCode());
    }
}
