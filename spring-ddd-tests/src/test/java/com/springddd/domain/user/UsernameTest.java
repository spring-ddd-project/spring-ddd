package com.springddd.domain.user;

import com.springddd.domain.user.exception.UsernameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    void constructor_WithValidValue_ShouldCreateUsername() {
        // When
        Username username = new Username("admin");

        // Then
        assertEquals("admin", username.value());
    }

    @Test
    void constructor_WithNullValue_ShouldThrowUsernameException() {
        // When & Then
        UsernameException exception = assertThrows(
            UsernameException.class,
            () -> new Username(null)
        );
        assertEquals(1000, exception.getCode());
        assertEquals("error.user.name.null", exception.getMessageKey());
    }

    @Test
    void constructor_WithEmptyValue_ShouldThrowUsernameException() {
        // When & Then
        UsernameException exception = assertThrows(
            UsernameException.class,
            () -> new Username("")
        );
        assertEquals(1000, exception.getCode());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        Username username1 = new Username("admin");
        Username username2 = new Username("admin");
        Username username3 = new Username("user");

        // Then
        assertEquals(username1, username2);
        assertNotEquals(username1, username3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        Username username1 = new Username("admin");
        Username username2 = new Username("admin");

        // Then
        assertEquals(username1.hashCode(), username2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        Username username = new Username("admin");

        // When
        String result = username.toString();

        // Then
        assertTrue(result.contains("admin"));
    }

    @Test
    void value_ShouldReturnCorrectString() {
        // Given
        String expectedValue = "testuser";
        Username username = new Username(expectedValue);

        // When
        String actualValue = username.value();

        // Then
        assertEquals(expectedValue, actualValue);
    }
}
