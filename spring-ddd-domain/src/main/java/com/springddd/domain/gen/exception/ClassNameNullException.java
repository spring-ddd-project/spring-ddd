package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ClassNameNullException extends DomainException {

    public ClassNameNullException() {
        super(ErrorCode.GEN_INFO_CLASS_NAME_NULL);
    }
}
