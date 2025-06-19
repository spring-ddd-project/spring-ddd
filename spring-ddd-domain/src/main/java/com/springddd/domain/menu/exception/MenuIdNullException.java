package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class MenuIdNullException extends DomainException {

    public MenuIdNullException() {
        super(ErrorCode.MENU_ID_NULL);
    }
}
