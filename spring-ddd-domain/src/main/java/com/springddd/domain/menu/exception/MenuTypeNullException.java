package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;

import static com.springddd.domain.util.ErrorCode.MENU_TYPE_NULL;

public class MenuTypeNullException extends DomainException {

    public MenuTypeNullException() {
        super(MENU_TYPE_NULL);
    }
}
