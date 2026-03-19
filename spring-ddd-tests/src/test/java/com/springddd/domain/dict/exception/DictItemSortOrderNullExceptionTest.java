package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictItemSortOrderNullExceptionTest {

    @Test
    void constructor_shouldSetCorrectErrorCode() {
        DictItemSortOrderNullException exception = new DictItemSortOrderNullException();

        assertEquals(ErrorCode.DICT_ITEM_SORTORDER_NULL, exception.getErrorCode());
        assertEquals(1407, exception.getCode());
        assertEquals("error.dict.item.sortOrder.null", exception.getMessageKey());
    }

    @Test
    void shouldBeDomainException() {
        DictItemSortOrderNullException exception = new DictItemSortOrderNullException();
        assertTrue(exception instanceof DomainException);
    }
}
