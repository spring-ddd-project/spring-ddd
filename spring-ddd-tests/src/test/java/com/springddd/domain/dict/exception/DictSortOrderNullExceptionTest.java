package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictSortOrderNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictSortOrderNullException exception = new DictSortOrderNullException();

        assertEquals(ErrorCode.DICT_SORTORDER_NULL, exception.getErrorCode());
        assertEquals(1402, exception.getCode());
        assertEquals("error.dict.sortOrder.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictSortOrderNullException exception = new DictSortOrderNullException();
        assertTrue(exception instanceof DomainException);
    }
}
