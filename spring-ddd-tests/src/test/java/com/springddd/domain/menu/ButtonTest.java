package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuPermissionNullException;
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
    void shouldThrowExceptionWhenPermissionIsNull() {
        assertThrows(MenuPermissionNullException.class, () -> {
            new Button(null, "/api/user/create");
        });
    }
}
