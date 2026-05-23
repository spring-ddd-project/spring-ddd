package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuDomainMenuStrategyTest {

    private final SysMenuDomainMenuStrategy strategy = new SysMenuDomainMenuStrategy();

    @Test
    @DisplayName("check 当 type 为 2 时应返回 true")
    void check_whenTypeIsTwo_shouldReturnTrue() {
        assertThat(strategy.check(2)).isTrue();
        assertThat(strategy.check(1)).isFalse();
    }

    @Test
    @DisplayName("handle 应设置 menu 和 extendInfo")
    void handle_shouldSetMenuAndExtendInfo() {
        Catalog catalog = new Catalog("/path", "Comp", "/redirect");
        Menu menu = new Menu("/path", "Comp", null, null, null);
        Button button = new Button("perm", "/api");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, "Title", "icon", 2, true, true);

        SysMenuDomain domain = strategy.handle("test", catalog, menu, button, extendInfo);

        assertThat(domain.getName()).isEqualTo("test");
        assertThat(domain.getMenu()).isNotNull();
        assertThat(domain.getMenu().menuPath()).isEqualTo("/path");
        assertThat(domain.getMenuExtendInfo()).isNotNull();
    }
}
