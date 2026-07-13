package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class RoleMenuDataScopeMenuIdNullException extends DomainException {

    public RoleMenuDataScopeMenuIdNullException() {
        super(ErrorCode.ROLE_MENU_DATA_SCOPE_MENU_ID_NULL);
    }
}
