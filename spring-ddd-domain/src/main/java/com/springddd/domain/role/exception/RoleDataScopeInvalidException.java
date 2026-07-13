package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RoleDataScopeInvalidException extends DomainException {

    public RoleDataScopeInvalidException() {
        super(ErrorCode.ROLE_DATA_SCOPE_INVALID);
    }
}
