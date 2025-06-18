package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RoleDataScopeNullException extends DomainException {

    public RoleDataScopeNullException() {
        super(ErrorCode.ROLE_DATA_SCOPE_NULL);
    }
}
