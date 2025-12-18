package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class MenuNameNullException extends DomainException {

    public MenuNameNullException() {
        super(ErrorCode.MENU_NAME_NULL);
    }
}
