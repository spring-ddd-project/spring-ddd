package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class UserPostPostIdNullException extends DomainException {

    public UserPostPostIdNullException() {
        super(ErrorCode.USER_POST_POST_ID_NULL);
    }
}
