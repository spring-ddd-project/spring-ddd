package com.springddd.domain;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException{

    private final int errorCode;
    private final String messageKey;
    private final Object[] args;

    public DomainException(int errorCode, String messageKey, Object... args) {
        super(messageKey);
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.args = args;
    }
}
