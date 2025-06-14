package com.springddd.domain.exception;

import lombok.Getter;

@Getter
public class UsernameException extends DomainException {

    public UsernameException() {
        super(1001, "用户名不能为空");
    }
}
