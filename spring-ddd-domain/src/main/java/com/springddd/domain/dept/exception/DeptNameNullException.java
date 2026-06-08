package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DeptNameNullException extends DomainException {

    public DeptNameNullException() {
        super(ErrorCode.DEPT_NAME_NULL);
    }
}
