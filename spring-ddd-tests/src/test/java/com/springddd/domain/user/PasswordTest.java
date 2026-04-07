package com.springddd.domain.user;

import com.springddd.domain.user.exception.PasswordNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void shouldCreatePasswordWithValidValue() {
        Password password = new Password("pass123");
        assertEquals("pass123", password.value());
    }

    @Test
    void shouldThrowWhenPasswordIsNull() {
        assertThrows(PasswordNullException.class, () -> new Password(null));
    }

    @Test
    void shouldThrowWhenPasswordIsEmpty() {
        assertThrows(PasswordNullException.class, () -> new Password(""));
    }

    @Test
    void equals_shouldWorkForSameValue() {
        Password password1 = new Password("pass123");
        Password password2 = new Password("pass123");
        assertEquals(password1, password2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        Password password1 = new Password("pass123");
        Password password2 = new Password("pass456");
        assertNotEquals(password1, password2);
    }

    @Test
    void toString_shouldReturnValue() {
        Password password = new Password("secret");
        assertEquals("Password[value=secret]", password.toString());
    }
}