package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuPermissionDeniedExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuPermissionDeniedException exception = new MenuPermissionDeniedException();
        assertEquals(ErrorCode.MENU_PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuPermissionDeniedException exception = new MenuPermissionDeniedException();
        assertEquals(1207, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuPermissionDeniedException exception = new MenuPermissionDeniedException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuPermissionDeniedException exception = new MenuPermissionDeniedException();
        assertEquals("error.menu.permission.denied", exception.getMessageKey());
    }
}
