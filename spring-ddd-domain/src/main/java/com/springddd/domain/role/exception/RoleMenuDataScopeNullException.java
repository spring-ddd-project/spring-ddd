package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RoleMenuDataScopeNullException extends DomainException {

    public RoleMenuDataScopeNullException() {
        super(ErrorCode.ROLE_MENU_DATA_SCOPE_NULL);
    }
}
