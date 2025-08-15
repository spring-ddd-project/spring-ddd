package com.springddd.domain.dict.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DictItemLabelNullException extends DomainException {

    public DictItemLabelNullException() {
        super(ErrorCode.DICT_ITEM_LABEL_NULL);
    }
}
