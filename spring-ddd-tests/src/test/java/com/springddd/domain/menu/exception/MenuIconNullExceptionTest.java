package com.springddd.domain.menu.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuIconNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        MenuIconNullException exception = new MenuIconNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MENU_ICON_NULL);
    }
}
