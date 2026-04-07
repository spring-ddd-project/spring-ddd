package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import com.springddd.domain.role.exception.RoleDataScopeNullException;
import com.springddd.domain.role.exception.RoleNameNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleBasicInfoTest {

    @Test
    void shouldCreateWithValidValues() {
        RoleBasicInfo info = new RoleBasicInfo("角色A", "role_a", 1, true);
        assertEquals("角色A", info.roleName());
        assertEquals("role_a", info.roleCode());
        assertEquals(1, info.roleDataScope());
        assertTrue(info.roleOwner());
    }

    @Test
    void shouldCreateWithZeroDataScope() {
        RoleBasicInfo info = new RoleBasicInfo("角色B", "role_b", 0, false);
        assertEquals(0, info.roleDataScope());
        assertFalse(info.roleOwner());
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(RoleNameNullException.class, () -> new RoleBasicInfo(null, "role_a", 1, true));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThrows(RoleNameNullException.class, () -> new RoleBasicInfo("", "role_a", 1, true));
    }

    @Test
    void shouldThrowWhenCodeIsNull() {
        assertThrows(RoleCodeNullException.class, () -> new RoleBasicInfo("角色A", null, 1, true));
    }

    @Test
    void shouldThrowWhenCodeIsEmpty() {
        assertThrows(RoleCodeNullException.class, () -> new RoleBasicInfo("角色A", "", 1, true));
    }

    @Test
    void shouldThrowWhenDataScopeIsNull() {
        assertThrows(RoleDataScopeNullException.class, () -> new RoleBasicInfo("角色A", "role_a", null, true));
    }

    @Test
    void equals_shouldWorkForSameValues() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_a", 1, true);
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentName() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色B", "role_a", 1, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentCode() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_b", 1, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentDataScope() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_a", 2, true);
        assertNotEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentRoleOwner() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_a", 1, false);
        assertNotEquals(info1, info2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        RoleBasicInfo info1 = new RoleBasicInfo("角色A", "role_a", 1, true);
        RoleBasicInfo info2 = new RoleBasicInfo("角色A", "role_a", 1, true);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        RoleBasicInfo info = new RoleBasicInfo("测试角色", "test_role", 5, false);
        String str = info.toString();
        assertTrue(str.contains("测试角色"));
        assertTrue(str.contains("test_role"));
    }
}
