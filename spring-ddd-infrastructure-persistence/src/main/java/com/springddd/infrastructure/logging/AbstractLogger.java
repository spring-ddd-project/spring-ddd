package com.springddd.infrastructure.logging;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractLogger {
    protected AbstractLogger next;

    public void setNext(AbstractLogger next) {
        this.next = next;
    }

    public void logMessage(int level, String message) {
        if (this.canHandle(level)) {
            write(message);
        }
        if (next != null) {
            next.logMessage(level, message);
        }
    }

    protected abstract boolean canHandle(int level);
    protected abstract void write(String message);
}
