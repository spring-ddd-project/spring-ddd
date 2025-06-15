package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;

public class RoleCodeNullException extends DomainException {

    public RoleCodeNullException() {
        super(1101, "角色编码不能为空");
    }
}
