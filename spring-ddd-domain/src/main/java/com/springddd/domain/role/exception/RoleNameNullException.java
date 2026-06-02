package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import lombok.Getter;

@Getter
public class RoleNameNullException extends DomainException {

    public RoleNameNullException() {
        super(ErrorCode.ROLE_NAME_NULL);
    }
}
