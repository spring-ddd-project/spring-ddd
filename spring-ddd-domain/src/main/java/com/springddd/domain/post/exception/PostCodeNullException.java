package com.springddd.domain.post.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class PostCodeNullException extends DomainException {

    public PostCodeNullException() {
        super(ErrorCode.POST_CODE_NULL);
    }
}
