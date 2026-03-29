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
    void shouldCreateRoleMenuIdWithLargeValue() {
        RoleMenuId roleMenuId = new RoleMenuId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, roleMenuId.value());
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
    void hashCode_shouldBeConsistent() {
        RoleMenuId id1 = new RoleMenuId(1L);
        RoleMenuId id2 = new RoleMenuId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        RoleMenuId roleMenuId = new RoleMenuId(456L);
        assertEquals("456", roleMenuId.toString());
    }
}
