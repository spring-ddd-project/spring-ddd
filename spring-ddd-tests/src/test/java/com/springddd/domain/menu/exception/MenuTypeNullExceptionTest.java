package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuTypeNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuTypeNullException exception = new MenuTypeNullException();
        assertEquals(ErrorCode.MENU_TYPE_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuTypeNullException exception = new MenuTypeNullException();
        assertEquals(1202, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuTypeNullException exception = new MenuTypeNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuTypeNullException exception = new MenuTypeNullException();
        assertEquals("error.menu.type.null", exception.getMessageKey());
    }
}
