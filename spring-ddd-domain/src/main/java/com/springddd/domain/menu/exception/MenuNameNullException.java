package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;

public class MenuNameNullException extends DomainException {

    public MenuNameNullException() {
        super(1200, "error.menu.name.null");
    }
}
