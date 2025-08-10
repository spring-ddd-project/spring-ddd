package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ComponentTypeNullException extends DomainException {

    public ComponentTypeNullException() {
        super(ErrorCode.GEN_BIND_COMPONENT_TYPE_NULL);
    }
}
