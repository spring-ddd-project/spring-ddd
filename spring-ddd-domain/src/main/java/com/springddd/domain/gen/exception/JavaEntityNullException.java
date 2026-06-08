package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class JavaEntityNullException extends DomainException {

    public JavaEntityNullException() {
        super(ErrorCode.GEN_INFO_JAVA_ENTITY_NULL);
    }
}
