package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictIdNullException extends DomainException {

    public DictIdNullException() {
        super(ErrorCode.GEN_INFO_DICT_ID_NULL);
    }
}