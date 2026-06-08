package com.springddd.domain.menu.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuPermissionDeniedExceptionTest {

    @Test
    void shouldThrowWithDefaultMessage() {
        assertThrows(MenuPermissionDeniedException.class, () -> {
            throw new MenuPermissionDeniedException();
        });
    }

    @Test
    void shouldCreateExceptionInstance() {
        MenuPermissionDeniedException exception = new MenuPermissionDeniedException();
        assertNotNull(exception);
    }

    @Test
    void toString_shouldReturnExceptionInfo() {
        MenuPermissionDeniedException exception = new MenuPermissionDeniedException();
        String str = exception.toString();
        assertTrue(str.contains("MenuPermissionDeniedException"));
    }
}
