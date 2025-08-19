package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ObjectTypeNullException extends DomainException {

    public ObjectTypeNullException() {
        super(ErrorCode.GEN_AGGREGATE_OBJECT_TYPE_NULL);
    }
}
