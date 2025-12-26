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
    ROLE_ID_NULL(1103, "error.role.id.null"),

    MENU_NAME_NULL(1200, "error.menu.name.null"),
    MENU_PERMISSION_NULL(1201, "error.menu.permission.null"),
    MENU_TYPE_NULL(1202, "error.menu.type.null"),
    MENU_ICON_NULL(1203, "error.menu.icon.null"),
    MENU_PATH_NULL(1204, "error.menu.path.null"),
    MENU_ID_NULL(1205, "error.menu.id.null"),
    MENU_COMPONENT_NULL(1206, "error.menu.component.null");

    private final int code;
    private final String messageKey;
}
