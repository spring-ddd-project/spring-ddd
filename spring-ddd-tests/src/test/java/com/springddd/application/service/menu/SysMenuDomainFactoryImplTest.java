package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysMenuDomainFactoryImplTest {

    @Mock
    private SysMenuDomainStrategy catalogStrategy;

    @Mock
    private SysMenuDomainStrategy menuStrategy;

    @Mock
    private SysMenuDomainStrategy buttonStrategy;

    private SysMenuDomainFactoryImpl factory;

    private MenuId parentId;
    private String name;
    private Catalog catalog;
    private Menu menu;
    private Button button;
    private MenuExtendInfo menuExtendInfo;
    private Long deptId;

    @BeforeEach
    void setUp() {
        factory = new SysMenuDomainFactoryImpl(Arrays.asList(catalogStrategy, menuStrategy, buttonStrategy));

        parentId = new MenuId(0L);
        name = "Test Menu";
        catalog = new Catalog("/index");
        menu = new Menu("/test", "TestComponent", true, false, false);
        button = new Button("test:view", "/api/test");
        menuExtendInfo = new MenuExtendInfo(1, "Test Title", "icon", 1, true, true);
        deptId = 100L;
    }

    @Test
    void create_shouldUseCatalogStrategyForType1() {
        when(catalogStrategy.check(1)).thenReturn(true);
        when(catalogStrategy.handle(eq(name), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class)))
                .thenAnswer(invocation -> {
                    SysMenuDomain domain = new SysMenuDomain();
                    domain.setName(name);
                    domain.setCatalog(invocation.getArgument(1));
                    return domain;
                });

        SysMenuDomain result = factory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(parentId, result.getParentId());
        assertEquals(deptId, result.getDeptId());
        assertFalse(result.getDeleteStatus());
        verify(catalogStrategy).check(1);
        verify(catalogStrategy).handle(eq(name), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class));
    }

    @Test
    void create_shouldUseMenuStrategyForType2() {
        MenuExtendInfo menuType2Info = new MenuExtendInfo(1, "Test", "icon", 2, true, true);

        when(menuStrategy.check(2)).thenReturn(true);
        when(menuStrategy.handle(eq(name), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class)))
                .thenAnswer(invocation -> {
                    SysMenuDomain domain = new SysMenuDomain();
                    domain.setName(name);
                    domain.setMenu(invocation.getArgument(2));
                    domain.setMenuExtendInfo(menuType2Info);
                    return domain;
                });

        SysMenuDomain result = factory.create(parentId, name, catalog, menu, button, menuType2Info, deptId);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(menuType2Info.menuType(), result.getMenuExtendInfo().menuType());
        verify(menuStrategy).check(2);
    }

    @Test
    void create_shouldUseButtonStrategyForType3() {
        MenuExtendInfo buttonTypeInfo = new MenuExtendInfo(1, 3, true);

        when(buttonStrategy.check(3)).thenReturn(true);
        when(buttonStrategy.handle(eq(name), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class)))
                .thenAnswer(invocation -> {
                    SysMenuDomain domain = new SysMenuDomain();
                    domain.setName(name);
                    domain.setButton(invocation.getArgument(3));
                    return domain;
                });

        SysMenuDomain result = factory.create(parentId, name, catalog, menu, button, buttonTypeInfo, deptId);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(buttonStrategy).check(3);
    }

    @Test
    void create_shouldSetDeleteStatusToFalse() {
        when(catalogStrategy.check(anyInt())).thenReturn(true);
        when(catalogStrategy.handle(anyString(), any(), any(), any(), any()))
                .thenReturn(new SysMenuDomain());

        SysMenuDomain result = factory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertFalse(result.getDeleteStatus());
    }

    @Test
    void create_shouldHandleNullMenuType() {
        MenuExtendInfo nullTypeInfo = new MenuExtendInfo(1, "Test", "icon", null, true, true);

        SysMenuDomain result = factory.create(parentId, name, catalog, menu, button, nullTypeInfo, deptId);

        assertNotNull(result);
        assertEquals(parentId, result.getParentId());
        assertEquals(deptId, result.getDeptId());
    }

    @Test
    void create_shouldSetCorrectDeptId() {
        when(catalogStrategy.check(anyInt())).thenReturn(true);
        when(catalogStrategy.handle(anyString(), any(), any(), any(), any()))
                .thenReturn(new SysMenuDomain());

        SysMenuDomain result = factory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertEquals(deptId, result.getDeptId());
    }

    @Test
    void create_shouldSetCorrectParentId() {
        when(catalogStrategy.check(anyInt())).thenReturn(true);
        when(catalogStrategy.handle(anyString(), any(), any(), any(), any()))
                .thenReturn(new SysMenuDomain());

        SysMenuDomain result = factory.create(parentId, name, catalog, menu, button, menuExtendInfo, deptId);

        assertEquals(parentId, result.getParentId());
    }
}
