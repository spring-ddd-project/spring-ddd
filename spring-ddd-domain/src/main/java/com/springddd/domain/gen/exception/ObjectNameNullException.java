package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ObjectNameNullException extends DomainException {

    public ObjectNameNullException() {
        super(ErrorCode.GEN_AGGREGATE_OBJECT_NAME_NULL);
    }
}
