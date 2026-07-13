package com.springddd.domain.user.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class UserPostUserIdNullException extends DomainException {

    public UserPostUserIdNullException() {
        super(ErrorCode.USER_POST_USER_ID_NULL);
    }
}
