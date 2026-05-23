package com.springddd.domain.gen.exception;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentTypeNullExceptionTest {

    @Test
    @DisplayName("构造异常应包含正确的错误码")
    void constructor_shouldHaveCorrectErrorCode() {
        ComponentTypeNullException exception = new ComponentTypeNullException();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.GEN_BIND_COMPONENT_TYPE_NULL);
    }
}
