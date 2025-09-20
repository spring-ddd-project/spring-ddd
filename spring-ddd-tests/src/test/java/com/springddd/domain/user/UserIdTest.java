package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void constructor_WithValidLongValue_ShouldCreateUserId() {
        // When
        UserId userId = new UserId(1L);

        // Then
        assertEquals(1L, userId.value());
    }

    @Test
    void constructor_WithNullValue_ShouldCreateUserIdWithNull() {
        // When
        UserId userId = new UserId(null);

        // Then
        assertNull(userId.value());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        UserId userId1 = new UserId(1L);
        UserId userId2 = new UserId(1L);
        UserId userId3 = new UserId(2L);

        // Then
        assertEquals(userId1, userId2);
        assertNotEquals(userId1, userId3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        UserId userId1 = new UserId(1L);
        UserId userId2 = new UserId(1L);

        // Then
        assertEquals(userId1.hashCode(), userId2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        UserId userId = new UserId(123L);

        // When
        String result = userId.toString();

        // Then
        assertTrue(result.contains("123"));
    }

    @Test
    void value_ShouldReturnCorrectLongValue() {
        // Given
        Long expectedValue = 999L;
        UserId userId = new UserId(expectedValue);

        // When
        Long actualValue = userId.value();

        // Then
        assertEquals(expectedValue, actualValue);
    }
}
