package com.springddd.domain.dept.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class DeptStatusNullException extends DomainException {

    public DeptStatusNullException() {
        super(ErrorCode.DEPT_STATUS_NULL);
    }
}



































