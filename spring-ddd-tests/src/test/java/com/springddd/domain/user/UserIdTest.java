package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void shouldCreateUserIdWithValidValue() {
        UserId userId = new UserId(1L);
        assertEquals(1L, userId.value());
    }

    @Test
    void shouldCreateUserIdWithZero() {
        UserId userId = new UserId(0L);
        assertEquals(0L, userId.value());
    }

    @Test
    void shouldCreateUserIdWithLargeValue() {
        UserId userId = new UserId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userId.value());
    }

    @Test
    void equals_shouldWorkForSameValue() {
        UserId id1 = new UserId(1L);
        UserId id2 = new UserId(1L);
        assertEquals(id1, id2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        UserId id1 = new UserId(1L);
        UserId id2 = new UserId(2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        UserId id1 = new UserId(1L);
        UserId id2 = new UserId(1L);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        UserId userId = new UserId(123L);
        assertEquals("123", userId.toString());
    }
}
