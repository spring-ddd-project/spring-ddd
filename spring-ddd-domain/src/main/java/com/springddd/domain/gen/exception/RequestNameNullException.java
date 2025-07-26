package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RequestNameNullException extends DomainException {

    public RequestNameNullException() {
        super(ErrorCode.GEN_INFO_REQUEST_NAME_NULL);
    }
}
