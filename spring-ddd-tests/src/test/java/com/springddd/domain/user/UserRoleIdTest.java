package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRoleIdTest {

    @Test
    void shouldCreateUserRoleIdWithValidValue() {
        UserRoleId userRoleId = new UserRoleId(1L);
        assertEquals(1L, userRoleId.value());
    }

    @Test
    void shouldCreateUserRoleIdWithZero() {
        UserRoleId userRoleId = new UserRoleId(0L);
        assertEquals(0L, userRoleId.value());
    }

    @Test
    void shouldCreateUserRoleIdWithLargeValue() {
        UserRoleId userRoleId = new UserRoleId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userRoleId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        UserRoleId id1 = new UserRoleId(1L);
        UserRoleId id2 = new UserRoleId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        UserRoleId id1 = new UserRoleId(1L);
        UserRoleId id2 = new UserRoleId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        UserRoleId id1 = new UserRoleId(1L);
        UserRoleId id2 = new UserRoleId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        UserRoleId userRoleId = new UserRoleId(123L);
        assertEquals("123", userRoleId.toString());
    }
}
