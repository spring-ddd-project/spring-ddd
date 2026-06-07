package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import com.springddd.domain.role.exception.RoleNameNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleBasicInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        RoleBasicInfo info = new RoleBasicInfo("角色A", "role_a", true);
        assertEquals("角色A", info.roleName());
        assertEquals("role_a", info.roleCode());
        assertTrue(info.roleOwner());
    }

    @Test
    void shouldCreateWithFalseOwner() {
        RoleBasicInfo info = new RoleBasicInfo("角色B", "role_b", false);
        assertFalse(info.roleOwner());
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(RoleNameNullException.class, () -> new RoleBasicInfo(null, "role_a", true));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThrows(RoleNameNullException.class, () -> new RoleBasicInfo("", "role_a", true));
    }

    @Test
    void shouldThrowWhenCodeIsNull() {
        assertThrows(RoleCodeNullException.class, () -> new RoleBasicInfo("角色A", null, true));
    }

    @Test
    void shouldThrowWhenCodeIsEmpty() {
        assertThrows(RoleCodeNullException.class, () -> new RoleBasicInfo("角色A", "", true));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_a", true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentName() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色B", "role_a", true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentCode() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_b", true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentRoleOwner() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_a", false);
        assertNotEquals(info1, info2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_a", true);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        RoleBasicInfo info = new RoleBasicInfo("测试角色", "test_role", false);
        String str = info.toString();
        assertTrue(str.contains("测试角色"));
        assertTrue(str.contains("test_role"));
    }
}
