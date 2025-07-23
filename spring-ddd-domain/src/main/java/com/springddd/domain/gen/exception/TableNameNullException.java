package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class TableNameNullException extends DomainException {

    public TableNameNullException() {
        super(ErrorCode.GEN_INFO_TABLE_NAME_NULL);
    }
}
