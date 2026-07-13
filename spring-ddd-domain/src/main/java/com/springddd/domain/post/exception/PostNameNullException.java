package com.springddd.domain.post.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class PostNameNullException extends DomainException {

    public PostNameNullException() {
        super(ErrorCode.POST_NAME_NULL);
    }
}
