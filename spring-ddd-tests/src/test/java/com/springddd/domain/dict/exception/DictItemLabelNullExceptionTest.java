package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictItemLabelNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictItemLabelNullException exception = new DictItemLabelNullException();

        assertEquals(ErrorCode.DICT_ITEM_LABEL_NULL, exception.getErrorCode());
        assertEquals(1405, exception.getCode());
        assertEquals("error.dict.item.label.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictItemLabelNullException exception = new DictItemLabelNullException();
        assertTrue(exception instanceof DomainException);
    }
}
