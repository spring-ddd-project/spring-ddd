package com.springddd.application.service.gen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnakeToCamelConverterTest {

    @Test
    @DisplayName("下划线命名应正确转换为驼峰命名")
    void convertToCamelCase_shouldConvertSnakeCase() {
        assertThat(SnakeToCamelConverter.convertToCamelCase("user_name")).isEqualTo("userName");
        assertThat(SnakeToCamelConverter.convertToCamelCase("sys_user_role")).isEqualTo("sysUserRole");
        assertThat(SnakeToCamelConverter.convertToCamelCase("id")).isEqualTo("id");
    }

    @Test
    @DisplayName("空字符串应返回原值")
    void convertToCamelCase_withEmptyString_shouldReturnEmpty() {
        assertThat(SnakeToCamelConverter.convertToCamelCase("")).isEmpty();
    }

    @Test
    @DisplayName("null 应返回 null")
    void convertToCamelCase_withNull_shouldReturnNull() {
        assertThat(SnakeToCamelConverter.convertToCamelCase(null)).isNull();
    }

    @Test
    @DisplayName("多个连续下划线应正确处理")
    void convertToCamelCase_withMultipleUnderscores_shouldHandleCorrectly() {
        assertThat(SnakeToCamelConverter.convertToCamelCase("user__name")).isEqualTo("userName");
    }

    @Test
    @DisplayName("首尾下划线应正确处理")
    void convertToCamelCase_withLeadingTrailingUnderscores_shouldHandleCorrectly() {
        assertThat(SnakeToCamelConverter.convertToCamelCase("_user_name_")).isEqualTo("UserName");
    }
}
