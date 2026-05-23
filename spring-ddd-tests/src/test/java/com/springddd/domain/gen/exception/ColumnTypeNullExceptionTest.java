package com.springddd.domain.gen.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnTypeNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        ColumnTypeNullException exception = new ColumnTypeNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.GEN_INFO_COLUMN_TYPE_NULL);
    }
}
