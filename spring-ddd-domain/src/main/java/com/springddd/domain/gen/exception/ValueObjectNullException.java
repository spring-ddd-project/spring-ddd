package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ValueObjectNullException extends DomainException {

    public ValueObjectNullException() {
        super(ErrorCode.GEN_INFO_VALUE_OBJECT_NULL);
    }
}
