package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleMenuIdTest {

    @Test
    void shouldCreateRoleMenuIdWithValidValue() {
        RoleMenuId roleMenuId = new RoleMenuId(1L);
        assertEquals(1L, roleMenuId.value());
    }

    @Test
    void shouldCreateRoleMenuIdWithZero() {
        RoleMenuId roleMenuId = new RoleMenuId(0L);
        assertEquals(0L, roleMenuId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        RoleMenuId id1 = new RoleMenuId(1L);
        RoleMenuId id2 = new RoleMenuId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        RoleMenuId id1 = new RoleMenuId(1L);
        RoleMenuId id2 = new RoleMenuId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        RoleMenuId roleMenuId = new RoleMenuId(456L);
        assertEquals("RoleMenuId[value=456]", roleMenuId.toString());
    }
}