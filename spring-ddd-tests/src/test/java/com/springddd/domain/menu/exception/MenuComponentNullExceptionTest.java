package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuComponentNullExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectErrorCode() {
        MenuComponentNullException exception = new MenuComponentNullException();
        assertEquals(ErrorCode.MENU_COMPONENT_NULL, exception.getErrorCode());
    }

    @Test
    void shouldHaveCorrectCode() {
        MenuComponentNullException exception = new MenuComponentNullException();
        assertEquals(1206, exception.getCode());
    }

    @Test
    void shouldBeInstanceOfDomainException() {
        MenuComponentNullException exception = new MenuComponentNullException();
        assertInstanceOf(DomainException.class, exception);
    }

    @Test
    void shouldHaveCorrectMessageKey() {
        MenuComponentNullException exception = new MenuComponentNullException();
        assertEquals("error.menu.component.null", exception.getMessageKey());
    }
}
