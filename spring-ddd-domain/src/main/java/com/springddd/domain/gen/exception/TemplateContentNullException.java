package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class TemplateContentNullException extends DomainException {

    public TemplateContentNullException() {
        super(ErrorCode.GEN_TEMPLATE_CONTENT_NULL);
    }
}
