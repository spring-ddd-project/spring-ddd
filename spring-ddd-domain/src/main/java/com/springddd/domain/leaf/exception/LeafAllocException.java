package com.springddd.domain.leaf.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public abstract class LeafAllocException extends DomainException {

    public LeafAllocException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
