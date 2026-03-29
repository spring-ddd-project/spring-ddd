package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldCreateAccountWithAllFields() {
        Account account = new Account();
        Username username = new Username("testuser");
        Password password = new Password("password123");

        account.setUsername(username);
        account.setPassword(password);
        account.setEmail("test@example.com");
        account.setPhone("13800138000");
        account.setLockStatus(false);

        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
        assertEquals("test@example.com", account.getEmail());
        assertEquals("13800138000", account.getPhone());
        assertFalse(account.getLockStatus());
    }

    @Test
    void shouldSetAndGetUsername() {
        Account account = new Account();
        Username username = new Username("admin");
        account.setUsername(username);
        assertEquals(username, account.getUsername());
    }

    @Test
    void shouldSetAndGetPassword() {
        Account account = new Account();
        Password password = new Password("secret");
        account.setPassword(password);
        assertEquals(password, account.getPassword());
    }

    @Test
    void shouldSetAndGetEmail() {
        Account account = new Account();
        account.setEmail("user@test.com");
        assertEquals("user@test.com", account.getEmail());
    }

    @Test
    void shouldSetAndGetPhone() {
        Account account = new Account();
        account.setPhone("13900139000");
        assertEquals("13900139000", account.getPhone());
    }

    @Test
    void shouldSetAndGetLockStatus() {
        Account account = new Account();
        assertNull(account.getLockStatus());

        account.setLockStatus(true);
        assertTrue(account.getLockStatus());

        account.setLockStatus(false);
        assertFalse(account.getLockStatus());
    }

    @Test
    void shouldHandleNullEmailAndPhone() {
        Account account = new Account();
        account.setEmail(null);
        account.setPhone(null);

        assertNull(account.getEmail());
        assertNull(account.getPhone());
    }
}
