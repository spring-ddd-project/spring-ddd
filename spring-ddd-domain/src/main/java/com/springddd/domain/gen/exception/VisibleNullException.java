package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class VisibleNullException extends DomainException {

    public VisibleNullException() {
        super(ErrorCode.GEN_INFO_VISIBLE_NULL);
    }
}
