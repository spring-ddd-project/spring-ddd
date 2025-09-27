package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuNameNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MenuNameNullException exception = new MenuNameNullException();
        assertNotNull(exception);
        assertEquals("error.menu.name.null", exception.getMessageKey());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MenuNameNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MenuNameNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MenuNameNullException exception = new MenuNameNullException();
        assertEquals(1200, exception.getCode());
    }
}
