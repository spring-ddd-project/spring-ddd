package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysMenuDomainFactoryImplTest {

    @Test
    @DisplayName("should create SysMenuDomain with correct fields set when no strategy matches")
    void createWithNoMatchingStrategy() {
        List<SysMenuDomainStrategy> strategies = Collections.emptyList();
        SysMenuDomainFactoryImpl factory = new SysMenuDomainFactoryImpl(strategies);

        MenuId parentId = new MenuId(1L);
        Catalog catalog = new Catalog("/path", "component", "/redirect");
        Menu menu = new Menu("/menu", "MenuComponent", false, false, false);
        Button button = new Button("perm:read");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "title", "icon", 1, true, true);
        Long deptId = 1L;

        SysMenuDomain domain = factory.create(parentId, "TestMenu", catalog, menu, button, menuExtendInfo, deptId);

        assertNotNull(domain);
        assertEquals(parentId, domain.getParentId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    @DisplayName("should delegate to matching strategy")
    void createWithMatchingStrategy() {
        SysMenuDomainStrategy strategy = mock(SysMenuDomainStrategy.class);
        SysMenuDomain expectedDomain = new SysMenuDomain();
        expectedDomain.setName("HandledMenu");

        when(strategy.check(1)).thenReturn(true);
        when(strategy.handle(any(), any(), any(), any(), any())).thenReturn(expectedDomain);

        List<SysMenuDomainStrategy> strategies = List.of(strategy);
        SysMenuDomainFactoryImpl factory = new SysMenuDomainFactoryImpl(strategies);

        MenuId parentId = new MenuId(1L);
        Catalog catalog = new Catalog("/path", "component", "/redirect");
        Menu menu = new Menu("/menu", "MenuComponent", false, false, false);
        Button button = new Button("perm:read");
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(1, "title", "icon", 1, true, true);
        Long deptId = 1L;

        SysMenuDomain domain = factory.create(parentId, "TestMenu", catalog, menu, button, menuExtendInfo, deptId);

        assertNotNull(domain);
        assertEquals("HandledMenu", domain.getName());
        assertEquals(parentId, domain.getParentId());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());

        verify(strategy).check(1);
        verify(strategy).handle("TestMenu", catalog, menu, button, menuExtendInfo);
    }
}
