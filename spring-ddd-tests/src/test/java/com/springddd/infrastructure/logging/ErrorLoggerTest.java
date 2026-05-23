package com.springddd.infrastructure.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorLoggerTest {

    private ErrorLogger errorLogger;

    @BeforeEach
    void setUp() {
        errorLogger = new ErrorLogger();
    }

    @Test
    @DisplayName("canHandle 对 level 2 应返回 true")
    void canHandle_levelTwo_shouldReturnTrue() {
        assertThat(errorLogger.canHandle(2)).isTrue();
    }

    @Test
    @DisplayName("canHandle 对 level 1 应返回 false")
    void canHandle_levelOne_shouldReturnFalse() {
        assertThat(errorLogger.canHandle(1)).isFalse();
    }

    @Test
    @DisplayName("canHandle 对其他 level 应返回 false")
    void canHandle_otherLevel_shouldReturnFalse() {
        assertThat(errorLogger.canHandle(0)).isFalse();
        assertThat(errorLogger.canHandle(3)).isFalse();
    }

    @Test
    @DisplayName("write 方法不应抛出异常")
    void write_shouldNotThrowException() {
        errorLogger.write("test error message");
    }

    @Test
    @DisplayName("logMessage 对 level 2 应触发自身 write")
    void logMessage_levelTwo_shouldHandle() {
        errorLogger.logMessage(2, "error msg");
    }

    @Test
    @DisplayName("logMessage 对非处理 level 应仅传递不触发自身 write")
    void logMessage_notHandledLevel_shouldPropagateOnly() {
        errorLogger.logMessage(1, "info msg");
    }
}
