package com.springddd.infrastructure.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractLoggerTest {

    private AbstractLogger logger;

    @BeforeEach
    void setUp() {
        logger = new AbstractLogger() {
            @Override
            protected boolean canHandle(int level) {
                return level == 1;
            }

            @Override
            protected void write(String message) {
                // test implementation
            }
        };
    }

    @Test
    @DisplayName("setNext 应设置下一个 logger")
    void setNext_shouldSetNextLogger() {
        AbstractLogger next = new AbstractLogger() {
            @Override
            protected boolean canHandle(int level) {
                return level == 2;
            }

            @Override
            protected void write(String message) {
            }
        };

        logger.setNext(next);
        // 验证责任链可执行
        logger.logMessage(2, "test");
    }

    @Test
    @DisplayName("logMessage 当自身可处理时应调用 write")
    void logMessage_whenCanHandle_shouldCallWrite() {
        TestableLogger spyLogger = new TestableLogger(1);

        spyLogger.logMessage(1, "test message");
        assertThat(spyLogger.isWriteCalled()).isTrue();
    }

    @Test
    @DisplayName("logMessage 当无 next 时不应抛出异常")
    void logMessage_whenNoNext_shouldNotThrow() {
        logger.logMessage(1, "test message");
    }

    @Test
    @DisplayName("logMessage 应在自身处理后继续传递给 next")
    void logMessage_shouldPropagateToNext() {
        TestableLogger first = new TestableLogger(1);
        TestableLogger second = new TestableLogger(2);
        first.setNext(second);

        first.logMessage(2, "test");

        assertThat(first.isWriteCalled()).isFalse(); // level 2, first 不处理
        assertThat(second.isWriteCalled()).isTrue();  // level 2, second 处理
    }

    @Test
    @DisplayName("logMessage 对可处理 level 应自身和 next 都处理")
    void logMessage_shouldHandleSelfAndPropagate() {
        TestableLogger first = new TestableLogger(1);
        TestableLogger second = new TestableLogger(1);
        first.setNext(second);

        first.logMessage(1, "test");

        assertThat(first.isWriteCalled()).isTrue();
        assertThat(second.isWriteCalled()).isTrue();
    }

    /**
     * 测试用的 Logger 实现
     */
    private static class TestableLogger extends AbstractLogger {
        private final int handleLevel;
        private boolean writeCalled = false;

        TestableLogger(int handleLevel) {
            this.handleLevel = handleLevel;
        }

        @Override
        protected boolean canHandle(int level) {
            return level == handleLevel;
        }

        @Override
        protected void write(String message) {
            writeCalled = true;
        }

        boolean isWriteCalled() {
            return writeCalled;
        }
    }
}
