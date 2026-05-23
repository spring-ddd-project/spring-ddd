package com.springddd.infrastructure.cache.interpreter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AndExpressionTest {

    @Test
    @DisplayName("interpret 应拼接两个表达式的结果")
    void interpret_shouldConcatenateTwoExpressions() {
        Expression expr1 = new TerminalExpression("user");
        Expression expr2 = new TerminalExpression("123");
        AndExpression andExpression = new AndExpression(expr1, expr2);

        String result = andExpression.interpret(Map.of());

        assertThat(result).isEqualTo("user:123");
    }

    @Test
    @DisplayName("interpret 当两个表达式都解析 context 时应正确拼接")
    void interpret_whenBothResolveContext_shouldConcatenateResolvedValues() {
        Expression expr1 = new TerminalExpression("type");
        Expression expr2 = new TerminalExpression("id");
        AndExpression andExpression = new AndExpression(expr1, expr2);

        String result = andExpression.interpret(Map.of("type", "user", "id", "456"));

        assertThat(result).isEqualTo("user:456");
    }

    @Test
    @DisplayName("interpret 当一个表达式解析 context 一个不解析时应混合拼接")
    void interpret_whenOneResolvesOneNot_shouldMixConcatenate() {
        Expression expr1 = new TerminalExpression("prefix");
        Expression expr2 = new TerminalExpression("suffix");
        AndExpression andExpression = new AndExpression(expr1, expr2);

        String result = andExpression.interpret(Map.of("prefix", "cache"));

        assertThat(result).isEqualTo("cache:suffix");
    }

    @Test
    @DisplayName("interpret 支持嵌套 AndExpression")
    void interpret_withNestedAndExpression_shouldWork() {
        Expression expr1 = new TerminalExpression("a");
        Expression expr2 = new TerminalExpression("b");
        AndExpression inner = new AndExpression(expr1, expr2);
        AndExpression outer = new AndExpression(inner, new TerminalExpression("c"));

        String result = outer.interpret(Map.of());

        assertThat(result).isEqualTo("a:b:c");
    }
}
