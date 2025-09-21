package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuIconNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuIconNullException exception = new MenuIconNullException();
        assertEquals(ErrorCode.MENU_ICON_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuIconNullException exception = new MenuIconNullException();
        assertEquals(1203, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuIconNullException exception = new MenuIconNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuIconNullException exception = new MenuIconNullException();
        assertEquals("error.menu.icon.null", exception.getMessageKey());
    }
}
