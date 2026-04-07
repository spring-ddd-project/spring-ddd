package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleStatusNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleExtendInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        RoleExtendInfo info = new RoleExtendInfo("角色描述A", true);
        assertEquals("角色描述A", info.roleDesc());
        assertTrue(info.roleStatus());
    }

    @Test
    void shouldCreateWithNullDesc() {
        RoleExtendInfo info = new RoleExtendInfo(null, true);
        assertNull(info.roleDesc());
        assertTrue(info.roleStatus());
    }

    @Test
    void shouldCreateWithEmptyDesc() {
        RoleExtendInfo info = new RoleExtendInfo("", true);
        assertEquals("", info.roleDesc());
        assertTrue(info.roleStatus());
    }

    @Test
    void shouldCreateWithFalseStatus() {
        RoleExtendInfo info = new RoleExtendInfo("角色描述", false);
        assertEquals("角色描述", info.roleDesc());
        assertFalse(info.roleStatus());
    }

    @Test
    void shouldThrowWhenStatusIsNull() {
        assertThrows(RoleStatusNullException.class, () -> new RoleExtendInfo("描述", null));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        RoleExtendInfo info1 = new RoleExtendInfo("描述", true);
        RoleExtendInfo info2 = new RoleExtendInfo("描述", true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentDesc() {
        RoleExtendInfo info1 = new RoleExtendInfo("描述A", true);
        RoleExtendInfo info2 = new RoleExtendInfo("描述B", true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentStatus() {
        RoleExtendInfo info1 = new RoleExtendInfo("描述", true);
        RoleExtendInfo info2 = new RoleExtendInfo("描述", false);
        assertNotEquals(info1, info2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        RoleExtendInfo info1 = new RoleExtendInfo("描述", true);
        RoleExtendInfo info2 = new RoleExtendInfo("描述", true);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        RoleExtendInfo info = new RoleExtendInfo("测试描述", false);
        String str = info.toString();
        assertTrue(str.contains("测试描述"));
    }
}
