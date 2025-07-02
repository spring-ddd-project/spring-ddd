package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;

public class MenuPermissionDeniedException extends DomainException {

    public MenuPermissionDeniedException() {
        super(ErrorCode.MENU_PERMISSION_DENIED);
    }
}
