package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class FilterComponentNullException extends DomainException {

    public FilterComponentNullException() {
        super(ErrorCode.GEN_INFO_FILTER_COMPONENT_NULL);
    }
}
