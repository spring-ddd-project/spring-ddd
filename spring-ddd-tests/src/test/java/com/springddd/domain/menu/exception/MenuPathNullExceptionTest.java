package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuPathNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuPathNullException exception = new MenuPathNullException();
        assertEquals(ErrorCode.MENU_PATH_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuPathNullException exception = new MenuPathNullException();
        assertEquals(1204, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuPathNullException exception = new MenuPathNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuPathNullException exception = new MenuPathNullException();
        assertEquals("error.menu.path.null", exception.getMessageKey());
    }
}
