package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictItemItemStatusNullException extends DomainException {

    public DictItemItemStatusNullException() {
        super(ErrorCode.DICT_ITEM_ITEMSTATUS_NULL);
    }
}
