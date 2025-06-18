package com.springddd.domain.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NAME_NULL(1000, "error.user.name.null"),
    USER_PASSWORD_NULL(1001, "error.user.password.null"),
    ROLE_CODE_NULL(1100, "error.role.code.null"),
    ROLE_NAME_NULL(1101, "error.role.name.null"),
    ROLE_DATA_SCOPE_NULL(1102, "error.role.dataScope.null"),
    MENU_NAME_NULL(1200, "error.menu.name.null");

    private final int code;
    private final String messageKey;
}
