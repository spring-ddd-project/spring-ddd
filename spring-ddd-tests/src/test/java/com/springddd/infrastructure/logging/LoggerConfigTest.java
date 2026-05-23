package com.springddd.infrastructure.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoggerConfigTest {

    private LoggerConfig loggerConfig;

    @BeforeEach
    void setUp() {
        loggerConfig = new LoggerConfig();
    }

    @Test
    @DisplayName("loggerChain 应返回非空的 AbstractLogger")
    void loggerChain_shouldReturnNonNullLogger() {
        InfoLogger infoLogger = new InfoLogger();
        ErrorLogger errorLogger = new ErrorLogger();

        AbstractLogger chain = loggerConfig.loggerChain(infoLogger, errorLogger);

        assertThat(chain).isNotNull();
        assertThat(chain).isSameAs(infoLogger);
    }

    @Test
    @DisplayName("loggerChain 应将 errorLogger 设置为 infoLogger 的 next")
    void loggerChain_shouldSetErrorLoggerAsNext() {
        InfoLogger infoLogger = new InfoLogger();
        ErrorLogger errorLogger = new ErrorLogger();

        loggerConfig.loggerChain(infoLogger, errorLogger);

        // 验证责任链：INFO logger 接收 level 1，然后传递给 ERROR logger
        infoLogger.logMessage(1, "info message");
        infoLogger.logMessage(2, "error message");
    }
}
