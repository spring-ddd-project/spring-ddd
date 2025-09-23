package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuComponentNullException;
import com.springddd.domain.menu.exception.MenuPathNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    void shouldCreateMenuWithAllFields() {
        String menuPath = "/user";
        String component = "UserView";
        Boolean affixTab = true;
        Boolean noBasicLayout = false;
        Boolean embedded = false;

        Menu menu = new Menu(menuPath, component, affixTab, noBasicLayout, embedded);

        assertEquals(menuPath, menu.menuPath());
        assertEquals(component, menu.component());
        assertEquals(affixTab, menu.affixTab());
        assertEquals(noBasicLayout, menu.noBasicLayout());
        assertEquals(embedded, menu.embedded());
    }

    @Test
    void shouldThrowExceptionWhenMenuPathIsNull() {
        assertThrows(MenuPathNullException.class, () -> {
            new Menu(null, "UserView", true, false, false);
        });
    }

    @Test
    void shouldThrowExceptionWhenComponentIsNull() {
        assertThrows(MenuComponentNullException.class, () -> {
            new Menu("/user", null, true, false, false);
        });
    }
}
