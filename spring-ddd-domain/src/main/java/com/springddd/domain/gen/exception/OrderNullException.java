package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class OrderNullException extends DomainException {

    public OrderNullException() {
        super(ErrorCode.GEN_INFO_ORDER_NULL);
    }
}
