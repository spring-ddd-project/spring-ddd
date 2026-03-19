package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void defaultConstructor_ShouldCreateEmptyAccount() {
        // When
        Account account = new Account();

        // Then
        assertNull(account.getUsername());
        assertNull(account.getPassword());
        assertNull(account.getEmail());
        assertNull(account.getPhone());
        assertNull(account.getLockStatus());
    }

    @Test
    void setters_ShouldUpdateValues() {
        // Given
        Account account = new Account();
        Username username = new Username("admin");
        Password password = new Password("password123");

        // When
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail("admin@example.com");
        account.setPhone("1234567890");
        account.setLockStatus(false);

        // Then
        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
        assertEquals("admin@example.com", account.getEmail());
        assertEquals("1234567890", account.getPhone());
        assertFalse(account.getLockStatus());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();

        account1.setEmail("admin@example.com");
        account2.setEmail("admin@example.com");
        account3.setEmail("other@example.com");

        // Then
        assertEquals(account1, account2);
        assertNotEquals(account1, account3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        Account account1 = new Account();
        Account account2 = new Account();

        account1.setEmail("admin@example.com");
        account2.setEmail("admin@example.com");

        // Then
        assertEquals(account1.hashCode(), account2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        Account account = new Account();
        account.setEmail("admin@example.com");

        // When
        String result = account.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    void lockStatus_ShouldToggleCorrectly() {
        // Given
        Account account = new Account();
        assertNull(account.getLockStatus());

        // When
        account.setLockStatus(true);

        // Then
        assertTrue(account.getLockStatus());

        // When
        account.setLockStatus(false);

        // Then
        assertFalse(account.getLockStatus());
    }
}
