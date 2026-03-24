package com.springddd.domain.user;

import com.springddd.domain.AggregateRootId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleIdTest {

    @Test
    void constructor_WithValidLongValue_ShouldCreateUserRoleId() {
        // When
        UserRoleId userRoleId = new UserRoleId(1L);

        // Then
        assertEquals(1L, userRoleId.value());
    }

    @Test
    void constructor_WithNullValue_ShouldCreateUserRoleIdWithNull() {
        // When
        UserRoleId userRoleId = new UserRoleId(null);

        // Then
        assertNull(userRoleId.value());
    }

    @Test
    void constructor_WithLargeValue_ShouldCreateUserRoleId() {
        // Given
        Long largeValue = 999999999L;

        // When
        UserRoleId userRoleId = new UserRoleId(largeValue);

        // Then
        assertEquals(largeValue, userRoleId.value());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        UserRoleId userRoleId1 = new UserRoleId(1L);
        UserRoleId userRoleId2 = new UserRoleId(1L);
        UserRoleId userRoleId3 = new UserRoleId(2L);

        // Then
        assertEquals(userRoleId1, userRoleId2);
        assertNotEquals(userRoleId1, userRoleId3);
    }

    @Test
    void equals_WithSameValue_ShouldBeEqual() {
        // Given
        UserRoleId id1 = new UserRoleId(100L);
        UserRoleId id2 = new UserRoleId(100L);

        // Then
        assertEquals(id1, id2);
    }

    @Test
    void equals_WithDifferentValues_ShouldNotBeEqual() {
        // Given
        UserRoleId id1 = new UserRoleId(100L);
        UserRoleId id2 = new UserRoleId(200L);

        // Then
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        UserRoleId userRoleId1 = new UserRoleId(1L);
        UserRoleId userRoleId2 = new UserRoleId(1L);

        // Then
        assertEquals(userRoleId1.hashCode(), userRoleId2.hashCode());
    }

    @Test
    void hashCode_WithDifferentValues_ShouldBeDifferent() {
        // Given
        UserRoleId id1 = new UserRoleId(1L);
        UserRoleId id2 = new UserRoleId(2L);

        // Then
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        UserRoleId userRoleId = new UserRoleId(123L);

        // When
        String result = userRoleId.toString();

        // Then
        assertTrue(result.contains("123"));
    }

    @Test
    void value_ShouldReturnCorrectLongValue() {
        // Given
        Long expectedValue = 999L;
        UserRoleId userRoleId = new UserRoleId(expectedValue);

        // When
        Long actualValue = userRoleId.value();

        // Then
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void implementsAggregateRootId() {
        // Given
        UserRoleId userRoleId = new UserRoleId(1L);

        // Then
        assertTrue(userRoleId instanceof AggregateRootId);
    }
}
