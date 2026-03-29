package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuPermissionNullExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        MenuPermissionNullException exception = new MenuPermissionNullException();
        assertNotNull(exception);
        assertEquals("error.menu.permission.null", exception.getMessageKey());
    }

    @Test
    void shouldExtendDomainException() {
        assertTrue(DomainException.class.isAssignableFrom(MenuPermissionNullException.class));
    }

    @Test
    void exception_shouldBeRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(MenuPermissionNullException.class));
    }

    @Test
    void shouldHaveCorrectErrorCode() {
        MenuPermissionNullException exception = new MenuPermissionNullException();
        assertEquals(1201, exception.getCode());
    }
}
