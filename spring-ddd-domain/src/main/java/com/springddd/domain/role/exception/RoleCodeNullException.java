package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RoleCodeNullException extends DomainException {

    public RoleCodeNullException() {
        super(ErrorCode.ROLE_CODE_NULL);
    }
}
