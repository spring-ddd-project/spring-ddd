package com.springddd.domain.post.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class SortOrderNullException extends DomainException {

    public SortOrderNullException() {
        super(ErrorCode.POST_SORT_ORDER_NULL);
    }
}
