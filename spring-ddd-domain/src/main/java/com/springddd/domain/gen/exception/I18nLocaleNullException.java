package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class I18nLocaleNullException extends DomainException {

    public I18nLocaleNullException() {
        super(ErrorCode.GEN_I18N_LOCALE_NULL);
    }
}
