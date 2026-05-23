package com.springddd.domain.menu.builder;

import com.springddd.domain.menu.SysMenuDomain;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuDomainBuilderTest {

    @Test
    void testBuildMinimal() {
        SysMenuDomain domain = new SysMenuDomainBuilder()
                .name("Test Menu")
                .build();
        assertThat(domain.getName()).isEqualTo("Test Menu");
    }

    @Test
    void testBuildFull() {
        SysMenuDomain domain = new SysMenuDomainBuilder()
                .menuId(1L)
                .parentId(2L)
                .name("Test Menu")
                .catalog("/path", "Layout", "/redirect")
                .menu("/menu", "MenuComponent", true, false, false)
                .button("sys:user:list")
                .advancedOptions(1, "icon", 1, true, true)
                .menuExtendInfo(100L)
                .deptId(10L)
                .build();

        assertThat(domain.getMenuId().value()).isEqualTo(1L);
        assertThat(domain.getParentId().value()).isEqualTo(2L);
        assertThat(domain.getName()).isEqualTo("Test Menu");
        assertThat(domain.getCatalog().routePath()).isEqualTo("/path");
        assertThat(domain.getMenu().menuPath()).isEqualTo("/menu");
        assertThat(domain.getButton().permission()).isEqualTo("sys:user:list");
        assertThat(domain.getAdvancedOptions().order()).isEqualTo(1);
        assertThat(domain.getMenuExtendInfo()).isNotNull();
        assertThat(domain.getDeptId()).isEqualTo(10L);
    }

    @Test
    void testNullMenuIdIgnored() {
        SysMenuDomain domain = new SysMenuDomainBuilder()
                .menuId(null)
                .name("Test")
                .build();
        assertThat(domain.getMenuId()).isNull();
    }

    @Test
    void testNullParentIdIgnored() {
        SysMenuDomain domain = new SysMenuDomainBuilder()
                .parentId(null)
                .name("Test")
                .build();
        assertThat(domain.getParentId()).isNull();
    }
}
