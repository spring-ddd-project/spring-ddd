package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuPathNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MenuPathNullException exception = new MenuPathNullException();
        assertNotNull(exception);
        assertEquals("error.menu.path.null", exception.getMessageKey());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MenuPathNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MenuPathNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MenuPathNullException exception = new MenuPathNullException();
        assertEquals(1204, exception.getCode());
    }
}
