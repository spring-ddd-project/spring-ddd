package com.springddd.domain.user;

import com.springddd.domain.user.exception.UsernameException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    void shouldCreateUsernameWithValidValue() {
        Username username = new Username("user1");
        assertEquals("user1", username.value());
    }

    @Test
    void shouldThrowWhenUsernameIsNull() {
        assertThrows(UsernameException.class, () -> new Username(null));
    }

    @Test
    void shouldThrowWhenUsernameIsEmpty() {
        assertThrows(UsernameException.class, () -> new Username(""));
    }

    @Test
    void equals_shouldWorkForSameValue() {
        Username username1 = new Username("user1");
        Username username2 = new Username("user1");
        assertEquals(username1, username2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        Username username1 = new Username("user1");
        Username username2 = new Username("user2");
        assertNotEquals(username1, username2);
    }

    @Test
    void toString_shouldReturnValue() {
        Username username = new Username("testuser");
        assertEquals("Username[value=testuser]", username.toString());
    }
}