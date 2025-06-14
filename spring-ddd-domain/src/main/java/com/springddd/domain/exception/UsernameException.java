package com.springddd.domain.exception;

import lombok.Getter;

@Getter
public class UsernameException extends RuntimeException {

    private final int errorCode;

    public UsernameException() {
        super("用户名不能为空");
        this.errorCode = 1000;
    }
}
