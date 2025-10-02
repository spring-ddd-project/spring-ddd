package com.springddd.infrastructure.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
    @Bean
    public AbstractLogger loggerChain(InfoLogger infoLogger, ErrorLogger errorLogger) {
        infoLogger.setNext(errorLogger);
        return infoLogger;
    }
}
