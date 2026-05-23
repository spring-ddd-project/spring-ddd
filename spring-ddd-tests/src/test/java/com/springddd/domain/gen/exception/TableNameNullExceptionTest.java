package com.springddd.domain.gen.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableNameNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        TableNameNullException exception = new TableNameNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.GEN_INFO_TABLE_NAME_NULL);
    }
}
