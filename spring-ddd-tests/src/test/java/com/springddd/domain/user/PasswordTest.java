package com.springddd.domain.user;

import com.springddd.domain.user.exception.PasswordNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void shouldCreatePasswordWithValidValue() {
        Password password = new Password("validPassword123");
        assertEquals("validPassword123", password.value());
    }

    @Test
    void shouldCreatePasswordWithSpecialChars() {
        Password password = new Password("pass@word!");
        assertEquals("pass@word!", password.value());
    }

    @Test
    void shouldThrowPasswordNullExceptionWhenNull() {
        assertThrows(PasswordNullException.class, () -> new Password(null));
    }

    @Test
    void shouldThrowPasswordNullExceptionWhenEmpty() {
        assertThrows(PasswordNullException.class, () -> new Password(""));
    }

    @Test
    void equals_shouldWorkForSameValue() {
        Password password1 = new Password("secret");
        Password password2 = new Password("secret");
        assertEquals(password1, password2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        Password password1 = new Password("pass1");
        Password password2 = new Password("pass2");
        assertNotEquals(password1, password2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        Password password1 = new Password("secret");
        Password password2 = new Password("secret");
        assertEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        Password password = new Password("myPassword");
        assertEquals("myPassword", password.toString());
    }
}
