package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class MenuPathNullException extends DomainException {

    public MenuPathNullException() {
        super(ErrorCode.MENU_PATH_NULL);
    }
}
