package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

    @Test
    void testErrorCodeValues() {
        assertThat(ErrorCode.USER_NAME_NULL.getCode()).isEqualTo(1000);
        assertThat(ErrorCode.USER_PASSWORD_NULL.getCode()).isEqualTo(1001);
        assertThat(ErrorCode.ROLE_CODE_NULL.getCode()).isEqualTo(1100);
        assertThat(ErrorCode.MENU_NAME_NULL.getCode()).isEqualTo(1200);
        assertThat(ErrorCode.DEPT_NAME_NULL.getCode()).isEqualTo(1300);
        assertThat(ErrorCode.DICT_NAME_NULL.getCode()).isEqualTo(1400);
        assertThat(ErrorCode.GEN_INFO_PACKAGE_NAME_NULL.getCode()).isEqualTo(1500);
    }

    @Test
    void testErrorCodeMessageKeys() {
        assertThat(ErrorCode.USER_NAME_NULL.getMessageKey()).isEqualTo("error.user.name.null");
        assertThat(ErrorCode.GEN_TEMPLATE_CONTENT_NULL.getMessageKey()).isEqualTo("error.gen.template.content.null");
    }

    @Test
    void testAllEnumValues() {
        ErrorCode[] values = ErrorCode.values();
        assertThat(values).isNotEmpty();
        assertThat(values.length).isGreaterThan(50);
    }
}
