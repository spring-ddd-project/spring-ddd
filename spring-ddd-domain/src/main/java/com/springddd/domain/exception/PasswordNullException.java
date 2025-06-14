package com.springddd.domain.exception;

import lombok.Getter;

@Getter
public class PasswordNullException extends DomainException {

    public PasswordNullException() {
        super(1000, "密码不能为空");
    }
}
