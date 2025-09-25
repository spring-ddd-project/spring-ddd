package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleIdTest {

    @Test
    void shouldCreateRoleIdWithValidValue() {
        RoleId roleId = new RoleId(1L);
        assertEquals(1L, roleId.value());
    }

    @Test
    void shouldCreateRoleIdWithZero() {
        RoleId roleId = new RoleId(0L);
        assertEquals(0L, roleId.value());
    }

    @Test
    void shouldCreateRoleIdWithLargeValue() {
        RoleId roleId = new RoleId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, roleId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        RoleId id1 = new RoleId(1L);
        RoleId id2 = new RoleId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        RoleId id1 = new RoleId(1L);
        RoleId id2 = new RoleId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        RoleId id1 = new RoleId(1L);
        RoleId id2 = new RoleId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        RoleId roleId = new RoleId(123L);
        assertEquals("RoleId[value=123]", roleId.toString());
    }
}
