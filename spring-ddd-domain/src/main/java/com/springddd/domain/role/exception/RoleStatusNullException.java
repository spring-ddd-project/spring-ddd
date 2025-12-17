package com.springddd.domain.role.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import lombok.Getter;

@Getter
public class RoleStatusNullException extends DomainException {

    public RoleStatusNullException() {
        super(ErrorCode.ROLE_STATUS_NULL);
    }
}












