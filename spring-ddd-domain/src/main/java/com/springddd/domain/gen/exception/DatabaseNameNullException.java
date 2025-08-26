package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DatabaseNameNullException extends DomainException {

    public DatabaseNameNullException() {
        super(ErrorCode.GEN_INFO_DATABASE_NAME_NULL);
    }
}
