package com.springddd.domain.menu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuDomainTest {

    @Test
    void shouldCreateSysMenuDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId id = new MenuId(1L);
        domain.setMenuId(id);
        assertEquals(id, domain.getMenuId());
    }

    @Test
    void shouldSetAndGetName() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setName("Test Menu");
        assertEquals("Test Menu", domain.getName());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCallCreate() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateSysMenuDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        MenuId parentId = new MenuId(0L);
        Catalog catalog = new Catalog("/dashboard");
        Menu menu = new Menu("/path", "component", true, false, false);
        Button button = new Button("perm:read", "/api/read");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "title", "icon", 1, true, true);

        domain.update(parentId, "Test Menu", catalog, menu, button, menuExtendInfo, 1L);

        assertEquals(parentId, domain.getParentId());
        assertEquals("Test Menu", domain.getName());
        assertEquals(catalog, domain.getCatalog());
        assertEquals(menu, domain.getMenu());
        assertEquals(button, domain.getButton());
        assertEquals(menuExtendInfo, domain.getMenuExtendInfo());
        assertEquals(1L, domain.getDeptId());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysMenuDomain domain = new SysMenuDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysMenuDomain"));
    }

    @Test
    void shouldShowWhenStateIsNullAndVisibleTrue() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, true, true));

        domain.show();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldShowWhenStateIsNullAndVisibleFalse() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, false, true));

        domain.show();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldShowWhenStateExists() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setState(new com.springddd.domain.menu.state.VisibleMenuState());

        domain.show();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldHideWhenStateIsNullAndVisibleTrue() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, true, true));

        domain.hide();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldHideWhenStateIsNullAndVisibleFalse() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, false, true));

        domain.hide();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldHideWhenStateExists() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setState(new com.springddd.domain.menu.state.HiddenMenuState());

        domain.hide();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldCloneWithNullFields() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(null);
        domain.setParentId(null);
        domain.setMenu(null);
        domain.setAdvancedOptions(null);

        SysMenuDomain clone = domain.clone();

        assertNotNull(clone);
        assertNull(clone.getMenuId());
        assertNull(clone.getParentId());
        assertNull(clone.getMenu());
        assertNull(clone.getAdvancedOptions());
    }

    @Test
    void shouldCloneWithAllFields() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setMenu(new Menu("/path", "component", true, false, false));
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, true, true));

        SysMenuDomain clone = domain.clone();

        assertNotNull(clone);
        assertEquals(1L, clone.getMenuId().value());
        assertEquals(0L, clone.getParentId().value());
        assertEquals("/path", clone.getMenu().menuPath());
        assertEquals(1, clone.getAdvancedOptions().order());
    }

    @Test
    void shouldHandleCloneNotSupportedException() {
        SysMenuDomain domain = new SysMenuDomain() {
            @Override
            protected Object doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("test");
            }
        };
        assertThrows(AssertionError.class, domain::clone);
    }

    @Test
    void shouldShowWhenStateIsNullAndAdvancedOptionsNull() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(null);

        domain.show();

        assertNotNull(domain.getState());
    }

    @Test
    void shouldHideWhenStateIsNullAndAdvancedOptionsNull() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(null);

        domain.hide();

        assertNotNull(domain.getState());
    }
}
