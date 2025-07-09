package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictDictStatusNullException extends DomainException {

    public DictDictStatusNullException() {
        super(ErrorCode.DICT_DICTSTATUS_NULL);
    }
}
