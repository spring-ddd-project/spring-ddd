package com.springddd.domain.dict.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictItemSortOrderNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        DictItemSortOrderNullException exception = new DictItemSortOrderNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DICT_ITEM_SORTORDER_NULL);
    }
}
