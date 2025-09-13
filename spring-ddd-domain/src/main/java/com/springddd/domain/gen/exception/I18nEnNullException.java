package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class I18nEnNullException extends DomainException {

    public I18nEnNullException() {
        super(ErrorCode.GEN_I18N_EN_NULL);
    }
}
