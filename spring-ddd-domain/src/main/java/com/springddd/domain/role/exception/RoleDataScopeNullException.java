package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;

public class RoleDataScopeNullException extends DomainException {

    public RoleDataScopeNullException() {
        super(1102, "角色数据权限不能为空");
    }
}
