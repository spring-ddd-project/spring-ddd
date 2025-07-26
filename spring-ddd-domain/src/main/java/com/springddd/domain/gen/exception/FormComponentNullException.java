package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class FormComponentNullException extends DomainException {

    public FormComponentNullException() {
        super(ErrorCode.GEN_INFO_FORM_COMPONENT_NULL);
    }
}