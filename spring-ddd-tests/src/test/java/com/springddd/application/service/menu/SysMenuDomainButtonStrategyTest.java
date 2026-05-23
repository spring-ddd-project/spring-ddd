package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuDomainButtonStrategyTest {

    private final SysMenuDomainButtonStrategy strategy = new SysMenuDomainButtonStrategy();

    @Test
    @DisplayName("check 当 type 为 3 时应返回 true")
    void check_whenTypeIsThree_shouldReturnTrue() {
        assertThat(strategy.check(3)).isTrue();
        assertThat(strategy.check(1)).isFalse();
    }

    @Test
    @DisplayName("handle 应设置 button 和 extendInfo")
    void handle_shouldSetButtonAndExtendInfo() {
        Catalog catalog = new Catalog("/path", "Comp", "/redirect");
        Menu menu = new Menu("/path", "Comp", null, null, null);
        Button button = new Button("perm", "/api");
        MenuExtendInfo extendInfo = new MenuExtendInfo(1, 3, true);

        SysMenuDomain domain = strategy.handle("test", catalog, menu, button, extendInfo);

        assertThat(domain.getName()).isEqualTo("test");
        assertThat(domain.getButton()).isNotNull();
        assertThat(domain.getButton().permission()).isEqualTo("perm");
        assertThat(domain.getMenuExtendInfo()).isNotNull();
    }
}
