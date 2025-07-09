package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictCodeNullException extends DomainException {

    public DictCodeNullException() {
        super(ErrorCode.DICT_CODE_NULL);
    }
}
