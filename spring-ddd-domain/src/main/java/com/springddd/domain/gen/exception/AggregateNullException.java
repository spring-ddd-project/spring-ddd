package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class AggregateNullException extends DomainException {

    public AggregateNullException() {
        super(ErrorCode.GEN_INFO_AGGREGATE_NULL);
    }
}
