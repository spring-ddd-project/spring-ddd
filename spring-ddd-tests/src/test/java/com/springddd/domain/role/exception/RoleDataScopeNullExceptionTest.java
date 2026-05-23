package com.springddd.domain.role.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleDataScopeNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        RoleDataScopeNullException exception = new RoleDataScopeNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ROLE_DATA_SCOPE_NULL);
    }
}
