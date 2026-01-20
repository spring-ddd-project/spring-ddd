package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ColumnNameNullException extends DomainException {

    public ColumnNameNullException() {
        super(ErrorCode.GEN_INFO_COLUMN_NAME_NULL);
    }
}
