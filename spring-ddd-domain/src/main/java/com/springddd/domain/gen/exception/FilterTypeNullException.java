package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class FilterTypeNullException extends DomainException {

    public FilterTypeNullException() {
        super(ErrorCode.GEN_INFO_FILTER_TYPE_NULL);
    }
}
