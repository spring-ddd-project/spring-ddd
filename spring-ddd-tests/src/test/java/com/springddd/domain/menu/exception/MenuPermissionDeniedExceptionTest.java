package com.springddd.domain.menu.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuPermissionDeniedExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        MenuPermissionDeniedException exception = new MenuPermissionDeniedException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MENU_PERMISSION_DENIED);
    }
}
