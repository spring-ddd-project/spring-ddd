package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ProjectNameNullException extends DomainException {

    public ProjectNameNullException() {
        super(ErrorCode.GEN_INFO_PROJECT_NAME_NULL);
    }
}
