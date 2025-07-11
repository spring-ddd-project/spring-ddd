package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictIdNullException extends DomainException {

    public DictIdNullException() {
        super(ErrorCode.DICT_ID_NULL);
    }
}
