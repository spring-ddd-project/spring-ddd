package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictItemValueNullException extends DomainException {

    public DictItemValueNullException() {
        super(ErrorCode.DICT_ITEM_VALUE_NULL);
    }
}
