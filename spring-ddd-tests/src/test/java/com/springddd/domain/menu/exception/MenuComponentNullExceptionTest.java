package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuComponentNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MenuComponentNullException exception = new MenuComponentNullException();
        assertNotNull(exception);
        assertEquals("error.menu.component.null", exception.getMessageKey());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MenuComponentNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MenuComponentNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MenuComponentNullException exception = new MenuComponentNullException();
        assertEquals(1206, exception.getCode());
    }
}
