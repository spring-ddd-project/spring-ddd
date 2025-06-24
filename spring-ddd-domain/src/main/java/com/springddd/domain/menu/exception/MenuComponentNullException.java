package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;

import static com.springddd.domain.util.ErrorCode.MENU_COMPONENT_NULL;

public class MenuComponentNullException extends DomainException {

    public MenuComponentNullException() {
        super(MENU_COMPONENT_NULL);
    }
}
