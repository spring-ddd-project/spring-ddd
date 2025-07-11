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
    MENU_COMPONENT_NULL(1206, "error.menu.component.null"),
    MENU_PERMISSION_DENIED(1207, "error.menu.permission.denied"),

    DEPT_NAME_NULL(1300, "error.dept.name.null"),
    DEPT_ID_NULL(1301, "error.dept.id.null"),

    DICT_NAME_NULL(1400, "error.dict.name.null"),
    DICT_CODE_NULL(1401, "error.dict.code.null"),
    DICT_SORTORDER_NULL(1402, "error.dict.sortOrder.null"),
    DICT_DICTSTATUS_NULL(1403, "error.dict.dictStatus.null"),
    DICT_ID_NULL(1404, "error.dict.id.null"),
    DICT_ITEM_LABEL_NULL(1405, "error.dict.item.label.null"),
    DICT_ITEM_VALUE_NULL(1406, "error.dict.item.value.null"),
    DICT_ITEM_SORTORDER_NULL(1407, "error.dict.item.sortOrder.null"),
    DICT_ITEM_ITEMSTATUS_NULL(1408, "error.dict.item.itemStatus.null")


    ;

    private final int code;
    private final String messageKey;
}
