package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuPermissionNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuPermissionNullException exception = new MenuPermissionNullException();
        assertEquals(ErrorCode.MENU_PERMISSION_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuPermissionNullException exception = new MenuPermissionNullException();
        assertEquals(1201, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuPermissionNullException exception = new MenuPermissionNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuPermissionNullException exception = new MenuPermissionNullException();
        assertEquals("error.menu.permission.null", exception.getMessageKey());
    }
}
