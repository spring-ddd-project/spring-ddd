package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class FormRequiredNullException extends DomainException {

    public FormRequiredNullException() {
        super(ErrorCode.GEN_INFO_FORM_REQUIRED_NULL);
    }
}
