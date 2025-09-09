package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class TypeScriptTypeNullException extends DomainException {

    public TypeScriptTypeNullException() {
        super(ErrorCode.GEN_BIND_TYPESCRIPT_TYPE_NULL);
    }
}
