package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import lombok.Getter;

@Getter
public class RoleNameNullException extends DomainException {

    public RoleNameNullException() {
        super(1100, "角色名称不能为空");
    }
}
