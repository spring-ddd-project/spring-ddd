package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DomainMaskNullException extends DomainException {

    public DomainMaskNullException() {
        super(ErrorCode.GEN_AGGREGATE_DOMAIN_MASK_NULL);
    }
}
