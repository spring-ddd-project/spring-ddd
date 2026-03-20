package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {

    @Test
    void testErrorCodeValues() {
        // Test that all error codes have correct code and messageKey
        assertEquals(1000, ErrorCode.USER_NAME_NULL.getCode());
        assertEquals("error.user.name.null", ErrorCode.USER_NAME_NULL.getMessageKey());

        assertEquals(1001, ErrorCode.USER_PASSWORD_NULL.getCode());
        assertEquals("error.user.password.null", ErrorCode.USER_PASSWORD_NULL.getMessageKey());

        assertEquals(1100, ErrorCode.ROLE_CODE_NULL.getCode());
        assertEquals("error.role.code.null", ErrorCode.ROLE_CODE_NULL.getMessageKey());

        assertEquals(1101, ErrorCode.ROLE_NAME_NULL.getCode());
        assertEquals("error.role.name.null", ErrorCode.ROLE_NAME_NULL.getMessageKey());
    }

    @Test
    void testAllErrorCodesHaveValidCode() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            assertTrue(errorCode.getCode() >= 1000, "Error code should be >= 1000");
            assertNotNull(errorCode.getMessageKey(), "Message key should not be null");
            assertFalse(errorCode.getMessageKey().isEmpty(), "Message key should not be empty");
        }
    }

    @Test
    void testMenuErrorCodes() {
        assertEquals(1200, ErrorCode.MENU_NAME_NULL.getCode());
        assertEquals(1201, ErrorCode.MENU_PERMISSION_NULL.getCode());
        assertEquals(1202, ErrorCode.MENU_TYPE_NULL.getCode());
        assertEquals(1203, ErrorCode.MENU_ICON_NULL.getCode());
        assertEquals(1204, ErrorCode.MENU_PATH_NULL.getCode());
        assertEquals(1205, ErrorCode.MENU_ID_NULL.getCode());
        assertEquals(1206, ErrorCode.MENU_COMPONENT_NULL.getCode());
        assertEquals(1207, ErrorCode.MENU_PERMISSION_DENIED.getCode());
    }

    @Test
    void testDeptErrorCodes() {
        assertEquals(1300, ErrorCode.DEPT_NAME_NULL.getCode());
        assertEquals(1301, ErrorCode.DEPT_ID_NULL.getCode());
    }

    @Test
    void testDictErrorCodes() {
        assertEquals(1400, ErrorCode.DICT_NAME_NULL.getCode());
        assertEquals(1401, ErrorCode.DICT_CODE_NULL.getCode());
        assertEquals(1402, ErrorCode.DICT_SORTORDER_NULL.getCode());
        assertEquals(1403, ErrorCode.DICT_DICTSTATUS_NULL.getCode());
        assertEquals(1404, ErrorCode.DICT_ID_NULL.getCode());
        assertEquals(1405, ErrorCode.DICT_ITEM_LABEL_NULL.getCode());
        assertEquals(1406, ErrorCode.DICT_ITEM_VALUE_NULL.getCode());
        assertEquals(1407, ErrorCode.DICT_ITEM_SORTORDER_NULL.getCode());
        assertEquals(1408, ErrorCode.DICT_ITEM_ITEMSTATUS_NULL.getCode());
    }

    @Test
    void testGenInfoErrorCodes() {
        assertEquals(1500, ErrorCode.GEN_INFO_PACKAGE_NAME_NULL.getCode());
        assertEquals(1501, ErrorCode.GEN_INFO_TABLE_NAME_NULL.getCode());
        assertEquals(1502, ErrorCode.GEN_INFO_CLASS_NAME_NULL.getCode());
        assertEquals(1503, ErrorCode.GEN_INFO_REQUEST_NAME_NULL.getCode());
        assertEquals(1504, ErrorCode.GEN_INFO_VALUE_OBJECT_NULL.getCode());
        assertEquals(1505, ErrorCode.GEN_INFO_COLUMN_KEY_NULL.getCode());
        assertEquals(1506, ErrorCode.GEN_INFO_COLUMN_NAME_NULL.getCode());
        assertEquals(1507, ErrorCode.GEN_INFO_COLUMN_TYPE_NULL.getCode());
        assertEquals(1508, ErrorCode.GEN_INFO_COLUMN_COMMENT_NULL.getCode());
        assertEquals(1509, ErrorCode.GEN_INFO_JAVA_ENTITY_NULL.getCode());
        assertEquals(1510, ErrorCode.GEN_INFO_JAVA_TYPE_NULL.getCode());
        assertEquals(1511, ErrorCode.GEN_INFO_DICT_ID_NULL.getCode());
        assertEquals(1512, ErrorCode.GEN_INFO_VISIBLE_NULL.getCode());
        assertEquals(1513, ErrorCode.GEN_INFO_ORDER_NULL.getCode());
        assertEquals(1514, ErrorCode.GEN_INFO_FILTER_NULL.getCode());
        assertEquals(1515, ErrorCode.GEN_INFO_FILTER_COMPONENT_NULL.getCode());
        assertEquals(1516, ErrorCode.GEN_INFO_FILTER_TYPE_NULL.getCode());
        assertEquals(1517, ErrorCode.GEN_INFO_FORM_COMPONENT_NULL.getCode());
        assertEquals(1518, ErrorCode.GEN_INFO_FORM_VISIBLE_NULL.getCode());
        assertEquals(1519, ErrorCode.GEN_INFO_FORM_REQUIRED_NULL.getCode());
        assertEquals(1520, ErrorCode.GEN_INFO_AGGREGATE_NULL.getCode());
    }

    @Test
    void testGenBindErrorCodes() {
        assertEquals(1521, ErrorCode.GEN_BIND_COMPONENT_TYPE_NULL.getCode());
        assertEquals(1522, ErrorCode.GEN_INFO_DATABASE_NAME_NULL.getCode());
    }

    @Test
    void testGenAggregateErrorCodes() {
        assertEquals(1523, ErrorCode.GEN_AGGREGATE_DOMAIN_MASK_NULL.getCode());
        assertEquals(1524, ErrorCode.GEN_AGGREGATE_OBJECT_NAME_NULL.getCode());
        assertEquals(1525, ErrorCode.GEN_AGGREGATE_OBJECT_TYPE_NULL.getCode());
    }

    @Test
    void testGenTemplateErrorCodes() {
        assertEquals(1526, ErrorCode.GEN_TEMPLATE_NAME_NULL.getCode());
        assertEquals(1527, ErrorCode.GEN_TEMPLATE_CONTENT_NULL.getCode());
    }

    @Test
    void testGenI18nErrorCodes() {
        assertEquals(1528, ErrorCode.GEN_BIND_TYPESCRIPT_TYPE_NULL.getCode());
        assertEquals(1529, ErrorCode.GEN_I18N_EN_NULL.getCode());
        assertEquals(1530, ErrorCode.GEN_I18N_LOCALE_NULL.getCode());
    }

    @Test
    void testGenInfoModuleAndProjectErrorCodes() {
        assertEquals(1531, ErrorCode.GEN_INFO_MODULE_NAME_NULL.getCode());
        assertEquals(1532, ErrorCode.GEN_INFO_PROJECT_NAME_NULL.getCode());
    }
}
