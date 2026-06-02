package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class TemplateNameNullException extends DomainException {

    public TemplateNameNullException() {
        super(ErrorCode.GEN_TEMPLATE_NAME_NULL);
    }
}
