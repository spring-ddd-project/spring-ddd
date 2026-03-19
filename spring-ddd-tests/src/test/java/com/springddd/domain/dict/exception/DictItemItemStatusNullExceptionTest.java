package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictItemItemStatusNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictItemItemStatusNullException exception = new DictItemItemStatusNullException();

        assertEquals(ErrorCode.DICT_ITEM_ITEMSTATUS_NULL, exception.getErrorCode());
        assertEquals(1408, exception.getCode());
        assertEquals("error.dict.item.itemStatus.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictItemItemStatusNullException exception = new DictItemItemStatusNullException();
        assertTrue(exception instanceof DomainException);
    }
}
