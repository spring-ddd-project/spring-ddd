package com.springddd.domain.role.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleNameNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        RoleNameNullException exception = new RoleNameNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ROLE_NAME_NULL);
    }
}
