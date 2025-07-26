package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ColumnTypeNullException extends DomainException {

    public ColumnTypeNullException() {
        super(ErrorCode.GEN_INFO_COLUMN_TYPE_NULL);
    }
}
