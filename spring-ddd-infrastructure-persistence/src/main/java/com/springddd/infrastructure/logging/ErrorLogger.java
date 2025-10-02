package com.springddd.infrastructure.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorLogger extends AbstractLogger {
    @Override
    protected boolean canHandle(int level) {
        return level == 2;
    }

    @Override
    protected void write(String message) {
        log.error("ERROR_LOGGER: " + message);
    }
}
