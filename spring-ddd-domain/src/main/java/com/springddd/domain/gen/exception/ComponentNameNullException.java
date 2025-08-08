package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ComponentNameNullException extends DomainException {

    public ComponentNameNullException() {
        super(ErrorCode.GEN_BIND_COMPONENT_NAME_NULL);
    }
}
