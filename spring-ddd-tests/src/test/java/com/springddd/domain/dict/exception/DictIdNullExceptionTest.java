package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictIdNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictIdNullException exception = new DictIdNullException();

        assertEquals(ErrorCode.DICT_ID_NULL, exception.getErrorCode());
        assertEquals(1404, exception.getCode());
        assertEquals("error.dict.id.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictIdNullException exception = new DictIdNullException();
        assertTrue(exception instanceof DomainException);
    }
}
