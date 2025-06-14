package com.springddd.domain.exception;

import lombok.Getter;

@Getter
public class PasswordNullException extends RuntimeException {

    private final int errorCode;

    public PasswordNullException() {
        super("密码不能为空");
        this.errorCode = 1001;
    }
}
