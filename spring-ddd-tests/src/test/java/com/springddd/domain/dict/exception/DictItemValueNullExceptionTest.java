package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictItemValueNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictItemValueNullException exception = new DictItemValueNullException();

        assertEquals(ErrorCode.DICT_ITEM_VALUE_NULL, exception.getErrorCode());
        assertEquals(1406, exception.getCode());
        assertEquals("error.dict.item.value.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictItemValueNullException exception = new DictItemValueNullException();
        assertTrue(exception instanceof DomainException);
    }
}
