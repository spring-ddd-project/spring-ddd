package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import lombok.Getter;

@Getter
public class PasswordNullException extends DomainException {

    public PasswordNullException() {
        super(1000, "error.user.password.null");
    }
}
