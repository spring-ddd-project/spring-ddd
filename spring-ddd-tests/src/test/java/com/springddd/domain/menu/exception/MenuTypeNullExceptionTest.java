package com.springddd.domain.menu.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTypeNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        MenuTypeNullException exception = new MenuTypeNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MENU_TYPE_NULL);
    }
}
