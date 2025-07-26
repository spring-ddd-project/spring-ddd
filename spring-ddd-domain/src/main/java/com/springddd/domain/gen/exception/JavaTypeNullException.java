package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class JavaTypeNullException extends DomainException {

    public JavaTypeNullException() {
        super(ErrorCode.GEN_INFO_JAVA_TYPE_NULL);
    }
}
