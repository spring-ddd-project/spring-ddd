package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuTypeNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MenuTypeNullException exception = new MenuTypeNullException();
        assertNotNull(exception);
        assertEquals("error.menu.type.null", exception.getMessageKey());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MenuTypeNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MenuTypeNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MenuTypeNullException exception = new MenuTypeNullException();
        assertEquals(1202, exception.getCode());
    }
}
