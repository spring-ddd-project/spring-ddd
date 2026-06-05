package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldCreateAccountWithAllArgs() {
        Username username = new Username("testuser");
        Password password = new Password("testpass");
        Account account = new Account(username, password, "test@example.com", "1234567890", true);
        assertNotNull(account);
        assertEquals(username, account.username());
        assertEquals(password, account.password());
        assertEquals("test@example.com", account.email());
        assertEquals("1234567890", account.phone());
        assertTrue(account.lockStatus());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        Account account1 = new Account(new Username("u"), new Password("p"), "test@example.com", null, false);
        Account account2 = new Account(new Username("u"), new Password("p"), "test@example.com", null, false);
        assertEquals(account1, account2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        Account account = new Account(new Username("u"), new Password("p"), null, null, false);
        String str = account.toString();
        assertTrue(str.contains("Account"));
    }
}
