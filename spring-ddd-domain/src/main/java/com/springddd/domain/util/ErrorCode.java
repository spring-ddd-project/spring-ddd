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
    DICT_ITEM_ITEMSTATUS_NULL(1408, "error.dict.item.itemStatus.null"),

    GEN_INFO_PACKAGE_NAME_NULL(1500, "error.gen.info.package.name.null"),
    GEN_INFO_TABLE_NAME_NULL(1501, "error.gen.info.table.name.null"),
    GEN_INFO_CLASS_NAME_NULL(1502, "error.gen.info.class.name.null"),
    GEN_INFO_REQUEST_NAME_NULL(1503, "error.gen.info.request.name.null"),
    GEN_INFO_VALUE_OBJECT_NULL(1504, "error.gen.info.value.object.null"),
    GEN_INFO_COLUMN_KEY_NULL(1505, "error.gen.info.column.key.null"),
    GEN_INFO_COLUMN_NAME_NULL(1506, "error.gen.info.column.name.null"),
    GEN_INFO_COLUMN_TYPE_NULL(1507, "error.gen.info.column.type.null"),
    GEN_INFO_COLUMN_COMMENT_NULL(1508, "error.gen.info.column.comment.null"),
    GEN_INFO_JAVA_ENTITY_NULL(1509, "error.gen.info.java.entity.null"),
    GEN_INFO_JAVA_TYPE_NULL(1510, "error.gen.info.java.type.null"),
    GEN_INFO_DICT_ID_NULL(1511, "error.gen.info.dict.id.null"),
    GEN_INFO_VISIBLE_NULL(1512, "error.gen.info.visible.null"),
    GEN_INFO_ORDER_NULL(1513, "error.gen.info.order.null"),
    GEN_INFO_FILTER_NULL(1514, "error.gen.info.filter.null"),
    GEN_INFO_FILTER_COMPONENT_NULL(1515, "error.gen.info.filter.component.null"),
    GEN_INFO_FILTER_TYPE_NULL(1516, "error.gen.info.filter.type.null"),
    GEN_INFO_FORM_COMPONENT_NULL(1517, "error.gen.info.form.component.null"),
    GEN_INFO_FORM_VISIBLE_NULL(1518, "error.gen.info.form.visible.null"),
    GEN_INFO_FORM_REQUIRED_NULL(1519, "error.gen.info.form.required.null"),
    GEN_INFO_AGGREGATE_NULL(1520, "error.gen.info.aggregate.null"),
    GEN_BIND_COMPONENT_TYPE_NULL(1521, "error.gen.bind.component.type.null"),
    GEN_INFO_DATABASE_NAME_NULL(1522, "error.gen.info.database.name.null"),

    ;

    private final int code;
    private final String messageKey;
}
