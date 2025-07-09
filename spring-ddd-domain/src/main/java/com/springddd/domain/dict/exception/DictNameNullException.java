package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictNameNullException extends DomainException {

    public DictNameNullException() {
        super(ErrorCode.DICT_NAME_NULL);
    }
}
