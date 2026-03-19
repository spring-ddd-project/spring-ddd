package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ButtonTest {

    @Test
    void shouldCreateButtonWithAllFields() {
        String permission = "user:create";
        String api = "/api/user/create";

        Button button = new Button(permission, api);

        assertEquals(permission, button.permission());
        assertEquals(api, button.api());
    }

    @Test
    void shouldHandleNullFields() {
        Button button = new Button(null, null);
        assertNull(button.permission());
        assertNull(button.api());
    }
}
