package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictSortOrderNullException extends DomainException {

    public DictSortOrderNullException() {
        super(ErrorCode.DICT_SORTORDER_NULL);
    }
}
