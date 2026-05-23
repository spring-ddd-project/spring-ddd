package com.springddd.application.service.gen.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InterfaceWebFilePathStrategyTest {

    private final InterfaceWebFilePathStrategy strategy = new InterfaceWebFilePathStrategy();
    private final String projectName = "spring-ddd";
    private final Map<String, Object> context = Map.of(
            "packageName", "com.springddd",
            "className", "SysUser"
    );

    @Test
    @DisplayName("supports should return true for controller template")
    void supports_controller_shouldReturnTrue() {
        assertThat(strategy.supports("controller")).isTrue();
    }

    @Test
    @DisplayName("supports should return false for unknown templates")
    void supports_unknownTemplates_shouldReturnFalse() {
        assertThat(strategy.supports("unknown")).isFalse();
        assertThat(strategy.supports("entity")).isFalse();
        assertThat(strategy.supports("sql")).isFalse();
        assertThat(strategy.supports("command")).isFalse();
    }

    @Test
    @DisplayName("generatePath should return correct path for controller template")
    void generatePath_controller_shouldReturnCorrectPath() {
        String path = strategy.generatePath("controller", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-interface-web/src/main/java/com/springddd/web/SysUserController.java");
    }
}
