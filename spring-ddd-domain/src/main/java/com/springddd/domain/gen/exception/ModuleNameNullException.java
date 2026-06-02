package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class ModuleNameNullException extends DomainException {

    public ModuleNameNullException() {
        super(ErrorCode.GEN_INFO_MODULE_NAME_NULL);
    }
}
