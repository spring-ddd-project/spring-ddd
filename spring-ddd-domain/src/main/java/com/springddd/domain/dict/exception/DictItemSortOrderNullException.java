package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictItemSortOrderNullException extends DomainException {

    public DictItemSortOrderNullException() {
        super(ErrorCode.DICT_ITEM_SORTORDER_NULL);
    }
}
