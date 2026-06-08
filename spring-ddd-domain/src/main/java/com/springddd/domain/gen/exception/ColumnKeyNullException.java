package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ColumnKeyNullException extends DomainException {

    public ColumnKeyNullException() {
        super(ErrorCode.GEN_INFO_COLUMN_KEY_NULL);
    }
}
