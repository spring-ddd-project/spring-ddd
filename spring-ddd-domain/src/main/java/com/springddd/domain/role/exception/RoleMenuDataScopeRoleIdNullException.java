package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RoleMenuDataScopeRoleIdNullException extends DomainException {

    public RoleMenuDataScopeRoleIdNullException() {
        super(ErrorCode.ROLE_MENU_DATA_SCOPE_ROLE_ID_NULL);
    }
}
