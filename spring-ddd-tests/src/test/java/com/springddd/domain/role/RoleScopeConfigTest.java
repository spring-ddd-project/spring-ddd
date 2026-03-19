package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleScopeConfigTest {

    @Test
    void constructor_WithValidParams_ShouldCreateSuccessfully() {
        // Given
        List<Long> depts = Arrays.asList(1L, 2L, 3L);
        List<Long> posts = Arrays.asList(10L, 20L);
        List<Long> users = Arrays.asList(100L, 200L, 300L);
        Boolean self = true;

        // When
        RoleScopeConfig config = new RoleScopeConfig(depts, posts, users, self);

        // Then
        assertEquals(depts, config.depts());
        assertEquals(posts, config.posts());
        assertEquals(users, config.users());
        assertTrue(config.self());
    }

    @Test
    void constructor_WithNullValues_ShouldCreateSuccessfully() {
        // When
        RoleScopeConfig config = new RoleScopeConfig(null, null, null, null);

        // Then
        assertNull(config.depts());
        assertNull(config.posts());
        assertNull(config.users());
        assertNull(config.self());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        RoleScopeConfig config1 = new RoleScopeConfig(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);
        RoleScopeConfig config2 = new RoleScopeConfig(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);
        RoleScopeConfig config3 = new RoleScopeConfig(null, null, null, null);

        // Then
        assertEquals(config1, config2);
        assertNotEquals(config1, config3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        RoleScopeConfig config1 = new RoleScopeConfig(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);
        RoleScopeConfig config2 = new RoleScopeConfig(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);

        // Then
        assertEquals(config1.hashCode(), config2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        RoleScopeConfig config = new RoleScopeConfig(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);

        // When
        String result = config.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    void depts_ShouldReturnCorrectList() {
        // Given
        List<Long> expectedDepts = Arrays.asList(5L, 10L, 15L);
        RoleScopeConfig config = new RoleScopeConfig(expectedDepts, null, null, null);

        // Then
        assertEquals(expectedDepts, config.depts());
    }

    @Test
    void self_ShouldReturnCorrectBoolean() {
        // Given
        RoleScopeConfig configTrue = new RoleScopeConfig(null, null, null, true);
        RoleScopeConfig configFalse = new RoleScopeConfig(null, null, null, false);

        // Then
        assertTrue(configTrue.self());
        assertFalse(configFalse.self());
    }
}
