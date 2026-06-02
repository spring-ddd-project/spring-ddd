package com.springddd.domain;

import com.springddd.domain.util.ErrorCode;
import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] args;

    public DomainException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.args = args;
    }

    public int getCode() {
        return errorCode.getCode();
    }

    public String getMessageKey() {
        return errorCode.getMessageKey();
    }
}
