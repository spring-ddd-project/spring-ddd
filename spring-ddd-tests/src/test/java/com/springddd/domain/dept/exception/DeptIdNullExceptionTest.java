package com.springddd.domain.dept.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeptIdNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        DeptIdNullException exception = new DeptIdNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DEPT_ID_NULL);
    }
}
