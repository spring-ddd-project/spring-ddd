package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RoleScopeConfigTest {

    @Test
    void shouldCreateWithValidValues() {
        List<Long> depts = Arrays.asList(1L, 2L);
        List<Long> posts = Arrays.asList(1L);
        List<Long> users = Arrays.asList(1L, 2L, 3L);

        RoleScopeConfig config = new RoleScopeConfig(depts, posts, users, true);

        assertEquals(depts, config.depts());
        assertEquals(posts, config.posts());
        assertEquals(users, config.users());
        assertTrue(config.self());
    }

    @Test
    void shouldCreateWithNullLists() {
        RoleScopeConfig config = new RoleScopeConfig(null, null, null, false);

        assertNull(config.depts());
        assertNull(config.posts());
        assertNull(config.users());
        assertFalse(config.self());
    }

    @Test
    void shouldCreateWithEmptyLists() {
        RoleScopeConfig config = new RoleScopeConfig(Arrays.asList(), Arrays.asList(), Arrays.asList(), true);

        assertNotNull(config.depts());
        assertTrue(config.depts().isEmpty());
        assertNotNull(config.posts());
        assertTrue(config.posts().isEmpty());
        assertNotNull(config.users());
        assertTrue(config.users().isEmpty());
        assertTrue(config.self());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        RoleScopeConfig config1 = new RoleScopeConfig(Arrays.asList(1L), Arrays.asList(1L), Arrays.asList(1L), true);
        RoleScopeConfig config2 = new RoleScopeConfig(Arrays.asList(1L), Arrays.asList(1L), Arrays.asList(1L), true);
        assertEquals(config1, config2);
    }

    @Test
    void equals_shouldFailForDifferentValues() {
        RoleScopeConfig config1 = new RoleScopeConfig(Arrays.asList(1L), Arrays.asList(1L), Arrays.asList(1L), true);
        RoleScopeConfig config2 = new RoleScopeConfig(Arrays.asList(2L), Arrays.asList(1L), Arrays.asList(1L), false);
        assertNotEquals(config1, config2);
    }

    @Test
    void toString_shouldReturnValues() {
        RoleScopeConfig config = new RoleScopeConfig(Arrays.asList(1L), Arrays.asList(1L), Arrays.asList(1L), true);
        String result = config.toString();
        assertTrue(result.contains("1"));
    }
}