package com.springddd.domain.menu.exception;

import com.springddd.domain.DomainException;
import static com.springddd.domain.util.ErrorCode.MENU_PERMISSION_NULL;

public class MenuPermissionNullException extends DomainException {

    public MenuPermissionNullException() {
        super(MENU_PERMISSION_NULL);
    }
}
