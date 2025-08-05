package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import lombok.Getter;

@Getter
public class UsernameException extends DomainException {

    public UsernameException() {
        super(ErrorCode.USER_NAME_NULL);
    }
}
