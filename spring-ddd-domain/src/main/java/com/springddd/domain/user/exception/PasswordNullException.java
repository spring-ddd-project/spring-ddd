package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import lombok.Getter;

import static com.springddd.domain.util.ErrorCode.USER_PASSWORD_NULL;

@Getter
public class PasswordNullException extends DomainException {

    public PasswordNullException() {
        super(USER_PASSWORD_NULL);
    }
}
