package com.springddd.domain.user.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        PasswordNullException exception = new PasswordNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_PASSWORD_NULL);
    }
}
