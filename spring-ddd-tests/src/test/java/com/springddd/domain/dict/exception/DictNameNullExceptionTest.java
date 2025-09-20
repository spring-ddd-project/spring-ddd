package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictNameNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictNameNullException exception = new DictNameNullException();

        assertEquals(ErrorCode.DICT_NAME_NULL, exception.getErrorCode());
        assertEquals(1400, exception.getCode());
        assertEquals("error.dict.name.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictNameNullException exception = new DictNameNullException();
        assertTrue(exception instanceof DomainException);
    }
}
