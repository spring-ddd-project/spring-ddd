package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuNameNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuNameNullException exception = new MenuNameNullException();
        assertEquals(ErrorCode.MENU_NAME_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuNameNullException exception = new MenuNameNullException();
        assertEquals(1200, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuNameNullException exception = new MenuNameNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuNameNullException exception = new MenuNameNullException();
        assertEquals("error.menu.name.null", exception.getMessageKey());
    }
}
