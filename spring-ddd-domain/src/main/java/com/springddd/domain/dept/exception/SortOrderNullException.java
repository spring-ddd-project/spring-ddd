package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class SortOrderNullException extends DomainException {

    public SortOrderNullException() {
        super(ErrorCode.DEPT_SORT_ORDER_NULL);
    }
}



































