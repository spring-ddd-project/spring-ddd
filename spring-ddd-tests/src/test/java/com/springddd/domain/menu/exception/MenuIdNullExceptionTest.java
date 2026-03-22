package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuIdNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuIdNullException exception = new MenuIdNullException();
        assertEquals(ErrorCode.MENU_ID_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuIdNullException exception = new MenuIdNullException();
        assertEquals(1205, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuIdNullException exception = new MenuIdNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuIdNullException exception = new MenuIdNullException();
        assertEquals("error.menu.id.null", exception.getMessageKey());
    }
}
