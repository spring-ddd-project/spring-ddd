package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DeptIdNullException extends DomainException {

    public DeptIdNullException() {
        super(ErrorCode.DEPT_ID_NULL);
    }
}
