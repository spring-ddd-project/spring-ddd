package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictDictStatusNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictDictStatusNullException exception = new DictDictStatusNullException();

        assertEquals(ErrorCode.DICT_DICTSTATUS_NULL, exception.getErrorCode());
        assertEquals(1403, exception.getCode());
        assertEquals("error.dict.dictStatus.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictDictStatusNullException exception = new DictDictStatusNullException();
        assertTrue(exception instanceof DomainException);
    }
}
