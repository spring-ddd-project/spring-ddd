package com.springddd.domain;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException{

    private final int errorCode;

    public DomainException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
