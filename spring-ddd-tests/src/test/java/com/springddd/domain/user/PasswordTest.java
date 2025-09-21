package com.springddd.domain.user;

import com.springddd.domain.user.exception.PasswordNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void constructor_WithValidValue_ShouldCreatePassword() {
        // When
        Password password = new Password("password123");

        // Then
        assertEquals("password123", password.value());
    }

    @Test
    void constructor_WithNullValue_ShouldThrowPasswordNullException() {
        // When & Then
        PasswordNullException exception = assertThrows(
            PasswordNullException.class,
            () -> new Password(null)
        );
        assertEquals(1001, exception.getCode());
        assertEquals("error.user.password.null", exception.getMessageKey());
    }

    @Test
    void constructor_WithEmptyValue_ShouldThrowPasswordNullException() {
        // When & Then
        PasswordNullException exception = assertThrows(
            PasswordNullException.class,
            () -> new Password("")
        );
        assertEquals(1001, exception.getCode());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        Password password1 = new Password("password123");
        Password password2 = new Password("password123");
        Password password3 = new Password("different");

        // Then
        assertEquals(password1, password2);
        assertNotEquals(password1, password3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        Password password1 = new Password("password123");
        Password password2 = new Password("password123");

        // Then
        assertEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        Password password = new Password("password123");

        // When
        String result = password.toString();

        // Then
        assertTrue(result.contains("password123"));
    }

    @Test
    void value_ShouldReturnCorrectString() {
        // Given
        String expectedValue = "secretpass";
        Password password = new Password(expectedValue);

        // When
        String actualValue = password.value();

        // Then
        assertEquals(expectedValue, actualValue);
    }
}
