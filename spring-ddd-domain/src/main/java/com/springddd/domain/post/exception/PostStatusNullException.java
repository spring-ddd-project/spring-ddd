package com.springddd.domain.post.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class PostStatusNullException extends DomainException {

    public PostStatusNullException() {
        super(ErrorCode.POST_STATUS_NULL);
    }
}
