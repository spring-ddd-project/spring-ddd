package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldCreateAccountWithDefaultConstructor() {
        Account account = new Account();
        assertNotNull(account);
    }

    @Test
    void shouldSetAndGetUsername() {
        Account account = new Account();
        Username username = new Username("testuser");
        account.setUsername(username);
        assertEquals(username, account.getUsername());
    }

    @Test
    void shouldSetAndGetPassword() {
        Account account = new Account();
        Password password = new Password("testpass");
        account.setPassword(password);
        assertEquals(password, account.getPassword());
    }

    @Test
    void shouldSetAndGetEmail() {
        Account account = new Account();
        account.setEmail("test@example.com");
        assertEquals("test@example.com", account.getEmail());
    }

    @Test
    void shouldSetAndGetPhone() {
        Account account = new Account();
        account.setPhone("1234567890");
        assertEquals("1234567890", account.getPhone());
    }

    @Test
    void shouldSetAndGetLockStatus() {
        Account account = new Account();
        account.setLockStatus(true);
        assertTrue(account.getLockStatus());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        Account account1 = new Account();
        account1.setEmail("test@example.com");
        Account account2 = new Account();
        account2.setEmail("test@example.com");
        assertEquals(account1, account2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        Account account = new Account();
        String str = account.toString();
        assertTrue(str.contains("Account"));
    }
}
