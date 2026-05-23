package com.springddd.application.service.gen.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFilePathStrategyTest {

    private final DefaultFilePathStrategy strategy = new DefaultFilePathStrategy();
    private final String projectName = "spring-ddd";
    private final Map<String, Object> context = Map.of(
            "className", "SysUser"
    );

    @Test
    @DisplayName("supports should return true for sql template")
    void supports_sql_shouldReturnTrue() {
        assertThat(strategy.supports("sql")).isTrue();
    }

    @Test
    @DisplayName("supports should return true for readme.txt template")
    void supports_readmeTxt_shouldReturnTrue() {
        assertThat(strategy.supports("readme.txt")).isTrue();
    }

    @Test
    @DisplayName("supports should return true for any .txt template")
    void supports_anyTxt_shouldReturnTrue() {
        assertThat(strategy.supports("custom.txt")).isTrue();
        assertThat(strategy.supports("notes.txt")).isTrue();
    }

    @Test
    @DisplayName("supports should return false for non-txt unknown templates")
    void supports_nonTxtUnknown_shouldReturnFalse() {
        assertThat(strategy.supports("unknown")).isFalse();
        assertThat(strategy.supports("controller")).isFalse();
        assertThat(strategy.supports("entity")).isFalse();
    }

    @Test
    @DisplayName("generatePath should return SQL.sql for sql template")
    void generatePath_sql_shouldReturnSqlPath() {
        String path = strategy.generatePath("sql", context, projectName);
        assertThat(path).isEqualTo("SQL.sql");
    }

    @Test
    @DisplayName("generatePath should return readme.txt for readme.txt template")
    void generatePath_readmeTxt_shouldReturnReadmePath() {
        String path = strategy.generatePath("readme.txt", context, projectName);
        assertThat(path).isEqualTo("readme.txt");
    }

    @Test
    @DisplayName("generatePath should return className.txt for other txt templates")
    void generatePath_otherTxt_shouldReturnClassNamePath() {
        String path = strategy.generatePath("custom.txt", context, projectName);
        assertThat(path).isEqualTo("SysUser.txt");
    }
}
