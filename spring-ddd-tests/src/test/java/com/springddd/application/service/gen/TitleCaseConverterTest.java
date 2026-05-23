package com.springddd.application.service.gen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TitleCaseConverterTest {

    @Test
    @DisplayName("下划线命名应正确转换为标题格式")
    void toTitleCase_shouldConvertSnakeCase() {
        assertThat(TitleCaseConverter.toTitleCase("user_name")).isEqualTo("User Name");
        assertThat(TitleCaseConverter.toTitleCase("sys_user_role")).isEqualTo("Sys User Role");
        assertThat(TitleCaseConverter.toTitleCase("id")).isEqualTo("Id");
    }

    @Test
    @DisplayName("空字符串应返回原值")
    void toTitleCase_withEmptyString_shouldReturnEmpty() {
        assertThat(TitleCaseConverter.toTitleCase("")).isEmpty();
    }

    @Test
    @DisplayName("null 应返回 null")
    void toTitleCase_withNull_shouldReturnNull() {
        assertThat(TitleCaseConverter.toTitleCase(null)).isNull();
    }

    @Test
    @DisplayName("连续下划线应跳过空部分")
    void toTitleCase_withMultipleUnderscores_shouldSkipEmptyParts() {
        assertThat(TitleCaseConverter.toTitleCase("user__name")).isEqualTo("User Name");
    }
}
