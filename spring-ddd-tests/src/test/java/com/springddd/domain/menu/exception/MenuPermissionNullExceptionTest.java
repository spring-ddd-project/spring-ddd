package com.springddd.domain.menu.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuPermissionNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        MenuPermissionNullException exception = new MenuPermissionNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MENU_PERMISSION_NULL);
    }
}
