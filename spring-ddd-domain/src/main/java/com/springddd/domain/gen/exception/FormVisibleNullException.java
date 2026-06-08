package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class FormVisibleNullException extends DomainException {

    public FormVisibleNullException() {
        super(ErrorCode.GEN_INFO_FORM_VISIBLE_NULL);
    }
}