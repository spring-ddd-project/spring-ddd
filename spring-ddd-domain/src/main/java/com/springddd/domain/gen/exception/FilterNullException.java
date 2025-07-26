package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class FilterNullException extends DomainException {

    public FilterNullException() {
        super(ErrorCode.GEN_INFO_FILTER_NULL);
    }
}
