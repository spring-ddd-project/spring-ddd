package com.springddd.domain.menu;

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
    void shouldHandleAllNullFields() {
        Menu menu = new Menu(null, null, null, null, null);
        assertNull(menu.menuPath());
        assertNull(menu.component());
        assertNull(menu.affixTab());
        assertNull(menu.noBasicLayout());
        assertNull(menu.embedded());
    }
}
