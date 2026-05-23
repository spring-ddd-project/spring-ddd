package com.springddd.domain.dict.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictSortOrderNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        DictSortOrderNullException exception = new DictSortOrderNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DICT_SORTORDER_NULL);
    }
}
