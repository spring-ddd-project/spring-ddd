package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuDomainStrategyTest {

    @Test
    void testButtonStrategyCheck() {
        SysMenuDomainButtonStrategy strategy = new SysMenuDomainButtonStrategy();
        assertThat(strategy.check(3)).isTrue();
        assertThat(strategy.check(1)).isFalse();
        assertThat(strategy.check(2)).isFalse();
    }

    @Test
    void testButtonStrategyHandle() {
        SysMenuDomainButtonStrategy strategy = new SysMenuDomainButtonStrategy();
        Button button = new Button("sys:user:list", "/api/user/list");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, 1, true);
        SysMenuDomain domain = strategy.handle("UserBtn", null, null, button, extendInfo);

        assertThat(domain.getName()).isEqualTo("UserBtn");
        assertThat(domain.getButton().permission()).isEqualTo("sys:user:list");
        assertThat(domain.getMenuExtendInfo().order()).isEqualTo(1);
    }

    @Test
    void testCatalogStrategyCheck() {
        SysMenuDomainCatalogStrategy strategy = new SysMenuDomainCatalogStrategy();
        assertThat(strategy.check(1)).isTrue();
        assertThat(strategy.check(2)).isFalse();
        assertThat(strategy.check(3)).isFalse();
    }

    @Test
    void testCatalogStrategyHandle() {
        SysMenuDomainCatalogStrategy strategy = new SysMenuDomainCatalogStrategy();
        Catalog catalog = new Catalog("/user", "Layout", "/user/index");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "User", "icon", 1, true, true);
        SysMenuDomain domain = strategy.handle("User", catalog, null, null, extendInfo);

        assertThat(domain.getName()).isEqualTo("User");
        assertThat(domain.getCatalog().routePath()).isEqualTo("/user");
        assertThat(domain.getMenuExtendInfo().title()).isEqualTo("User");
    }

    @Test
    void testMenuStrategyCheck() {
        SysMenuDomainMenuStrategy strategy = new SysMenuDomainMenuStrategy();
        assertThat(strategy.check(2)).isTrue();
        assertThat(strategy.check(1)).isFalse();
        assertThat(strategy.check(3)).isFalse();
    }

    @Test
    void testMenuStrategyHandle() {
        SysMenuDomainMenuStrategy strategy = new SysMenuDomainMenuStrategy();
        Menu menu = new Menu("/user/list", "UserList", true, false, false);
        MenuExtendInfo extendInfo = new MenuExtendInfo(2, "List", "icon", 2, true, true);
        SysMenuDomain domain = strategy.handle("UserList", null, menu, null, extendInfo);

        assertThat(domain.getName()).isEqualTo("UserList");
        assertThat(domain.getMenu().menuPath()).isEqualTo("/user/list");
        assertThat(domain.getMenuExtendInfo().menuType()).isEqualTo(2);
    }
}
