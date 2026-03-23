package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictCodeNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictCodeNullException exception = new DictCodeNullException();

        assertEquals(ErrorCode.DICT_CODE_NULL, exception.getErrorCode());
        assertEquals(1401, exception.getCode());
        assertEquals("error.dict.code.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictCodeNullException exception = new DictCodeNullException();
        assertTrue(exception instanceof DomainException);
    }

    @Test
    void shouldBeRuntimeException() {
        DictCodeNullException exception = new DictCodeNullException();
        assertTrue(exception instanceof RuntimeException);
    }
}
