package com.springddd.domain.user;

import com.springddd.domain.user.exception.UsernameException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    void shouldCreateUsernameWithValidValue() {
        Username username = new Username("validUser");
        assertEquals("validUser", username.value());
    }

    @Test
    void shouldCreateUsernameWithAlphanumeric() {
        Username username = new Username("user123");
        assertEquals("user123", username.value());
    }

    @Test
    void shouldCreateUsernameWithUnderscore() {
        Username username = new Username("user_name");
        assertEquals("user_name", username.value());
    }

    @Test
    void shouldThrowUsernameExceptionWhenNull() {
        assertThrows(UsernameException.class, () -> new Username(null));
    }

    @Test
    void shouldThrowUsernameExceptionWhenEmpty() {
        assertThrows(UsernameException.class, () -> new Username(""));
    }

    @Test
    void equals_shouldWorkForSameValue() {
        Username username1 = new Username("testuser");
        Username username2 = new Username("testuser");
        assertEquals(username1, username2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        Username username1 = new Username("user1");
        Username username2 = new Username("user2");
        assertNotEquals(username1, username2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        Username username1 = new Username("testuser");
        Username username2 = new Username("testuser");
        assertEquals(username1.hashCode(), username2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        Username username = new Username("admin");
        assertEquals("admin", username.toString());
    }
}
