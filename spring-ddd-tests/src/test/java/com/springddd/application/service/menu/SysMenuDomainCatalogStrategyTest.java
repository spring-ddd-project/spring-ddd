package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuDomainCatalogStrategyTest {

    private final SysMenuDomainCatalogStrategy strategy = new SysMenuDomainCatalogStrategy();

    @Test
    @DisplayName("check 当 type 为 1 时应返回 true")
    void check_whenTypeIsOne_shouldReturnTrue() {
        assertThat(strategy.check(1)).isTrue();
        assertThat(strategy.check(2)).isFalse();
    }

    @Test
    @DisplayName("handle 应设置 catalog 和 extendInfo")
    void handle_shouldSetCatalogAndExtendInfo() {
        Catalog catalog = new Catalog("/path", "Comp", "/redirect");
        Menu menu = new Menu("/path", "Comp", null, null, null);
        Button button = new Button("perm", "/api");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", "icon", 1, true, true);

        SysMenuDomain domain = strategy.handle("test", catalog, menu, button, extendInfo);

        assertThat(domain.getName()).isEqualTo("test");
        assertThat(domain.getCatalog()).isNotNull();
        assertThat(domain.getCatalog().routePath()).isEqualTo("/path");
        assertThat(domain.getMenuExtendInfo()).isNotNull();
        assertThat(domain.getMenuExtendInfo().title()).isEqualTo("Title");
    }
}
