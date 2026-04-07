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
    void toString_shouldReturnValueAsString() {
        SysMenuDomain domain = new SysMenuDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysMenuDomain"));
    }
}
