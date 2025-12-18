package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class MenuIconNullException extends DomainException {

    public MenuIconNullException() {
        super(ErrorCode.MENU_ICON_NULL);
    }
}
