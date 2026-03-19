package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdvancedOptionsTest {

    @Test
    void shouldCreateAdvancedOptionsWithAllFields() {
        Integer order = 1;
        String icon = "setting";
        Integer menuType = 1;
        Boolean visible = true;
        Boolean menuStatus = true;

        AdvancedOptions options = new AdvancedOptions(order, icon, menuType, visible, menuStatus);

        assertEquals(order, options.order());
        assertEquals(icon, options.icon());
        assertEquals(menuType, options.menuType());
        assertEquals(visible, options.visible());
        assertEquals(menuStatus, options.menuStatus());
    }

    @Test
    void shouldCreateAdvancedOptionsViaCatalogConstructor() {
        Integer order = 2;
        Integer menuType = 0;
        String icon = "system";
        Boolean menuStatus = true;
        Boolean visible = false;

        AdvancedOptions options = new AdvancedOptions(order, menuType, icon, menuStatus, visible);

        assertEquals(order, options.order());
        assertEquals(icon, options.icon());
        assertEquals(menuType, options.menuType());
        assertEquals(visible, options.visible());
        assertEquals(menuStatus, options.menuStatus());
    }

    @Test
    void shouldCreateAdvancedOptionsViaButtonConstructor() {
        Integer order = 3;
        Integer menuType = 2;
        Boolean menuStatus = false;

        AdvancedOptions options = new AdvancedOptions(order, menuType, menuStatus);

        assertEquals(order, options.order());
        assertNull(options.icon());
        assertEquals(menuType, options.menuType());
        assertNull(options.visible());
        assertEquals(menuStatus, options.menuStatus());
    }

    @Test
    void shouldHandleNullFieldsInPrimaryConstructor() {
        AdvancedOptions options = new AdvancedOptions(null, (String) null, null, null, null);
        assertNull(options.order());
        assertNull(options.icon());
        assertNull(options.menuType());
        assertNull(options.visible());
        assertNull(options.menuStatus());
    }

    @Test
    void shouldHandleZeroOrder() {
        AdvancedOptions options = new AdvancedOptions(0, "home", 1, true, true);
        assertEquals(0, options.order());
    }
}
