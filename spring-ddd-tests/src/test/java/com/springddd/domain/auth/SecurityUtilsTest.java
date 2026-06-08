package com.springddd.domain.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @Test
    void shouldHaveSecurityUtilsClass() {
        assertNotNull(SecurityUtils.class);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SecurityUtils utils = new SecurityUtils();
        String str = utils.toString();
        assertTrue(str.contains("SecurityUtils"));
    }
}
