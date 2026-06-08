package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RoleIdNullException extends DomainException {

    public RoleIdNullException() {
        super(ErrorCode.ROLE_ID_NULL);
    }
}
