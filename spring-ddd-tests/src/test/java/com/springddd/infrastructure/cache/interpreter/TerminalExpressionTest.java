package com.springddd.infrastructure.cache.interpreter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TerminalExpressionTest {

    @Test
    @DisplayName("interpret 当 context 包含 key 时应返回对应值")
    void interpret_whenContextContainsKey_shouldReturnValue() {
        TerminalExpression expression = new TerminalExpression("name");
        Map<String, Object> context = Map.of("name", "zhangsan");

        String result = expression.interpret(context);

        assertThat(result).isEqualTo("zhangsan");
    }

    @Test
    @DisplayName("interpret 当 context 不包含 key 时应返回 key 本身")
    void interpret_whenContextDoesNotContainKey_shouldReturnKey() {
        TerminalExpression expression = new TerminalExpression("name");
        Map<String, Object> context = new HashMap<>();

        String result = expression.interpret(context);

        assertThat(result).isEqualTo("name");
    }

    @Test
    @DisplayName("interpret 当值为非字符串类型时应转换为字符串")
    void interpret_whenValueIsNonString_shouldConvertToString() {
        TerminalExpression expression = new TerminalExpression("age");
        Map<String, Object> context = Map.of("age", 25);

        String result = expression.interpret(context);

        assertThat(result).isEqualTo("25");
    }

    @Test
    @DisplayName("interpret 当值为 null 时应返回字符串 null")
    void interpret_whenValueIsNull_shouldReturnStringNull() {
        TerminalExpression expression = new TerminalExpression("empty");
        Map<String, Object> context = new HashMap<>();
        context.put("empty", null);

        String result = expression.interpret(context);

        assertThat(result).isEqualTo("null");
    }
}
