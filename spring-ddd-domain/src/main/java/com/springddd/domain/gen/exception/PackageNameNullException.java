package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class PackageNameNullException extends DomainException {

    public PackageNameNullException() {
        super(ErrorCode.GEN_INFO_PACKAGE_NAME_NULL);
    }
}
