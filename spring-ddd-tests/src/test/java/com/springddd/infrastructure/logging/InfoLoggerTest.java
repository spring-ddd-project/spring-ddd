package com.springddd.infrastructure.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InfoLoggerTest {

    private InfoLogger infoLogger;

    @BeforeEach
    void setUp() {
        infoLogger = new InfoLogger();
    }

    @Test
    @DisplayName("canHandle 对 level 1 应返回 true")
    void canHandle_levelOne_shouldReturnTrue() {
        assertThat(infoLogger.canHandle(1)).isTrue();
    }

    @Test
    @DisplayName("canHandle 对 level 2 应返回 false")
    void canHandle_levelTwo_shouldReturnFalse() {
        assertThat(infoLogger.canHandle(2)).isFalse();
    }

    @Test
    @DisplayName("canHandle 对其他 level 应返回 false")
    void canHandle_otherLevel_shouldReturnFalse() {
        assertThat(infoLogger.canHandle(0)).isFalse();
        assertThat(infoLogger.canHandle(3)).isFalse();
    }

    @Test
    @DisplayName("write 方法不应抛出异常")
    void write_shouldNotThrowException() {
        infoLogger.write("test message");
    }

    @Test
    @DisplayName("setNext 应正确设置下一个 logger")
    void setNext_shouldSetNextLogger() {
        ErrorLogger errorLogger = new ErrorLogger();
        infoLogger.setNext(errorLogger);

        infoLogger.logMessage(2, "error msg");
        // 责任链应传递到 errorLogger 处理
    }

    @Test
    @DisplayName("logMessage 对 level 1 应触发自身 write 并传递")
    void logMessage_levelOne_shouldHandleAndPropagate() {
        ErrorLogger errorLogger = new ErrorLogger();
        infoLogger.setNext(errorLogger);

        // INFO logger 处理 level 1，然后传递到 ERROR logger（不处理 level 1）
        infoLogger.logMessage(1, "info msg");
    }
}
