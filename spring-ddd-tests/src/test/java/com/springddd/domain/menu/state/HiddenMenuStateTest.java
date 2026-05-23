package com.springddd.domain.menu.state;

import com.springddd.domain.menu.AdvancedOptions;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HiddenMenuStateTest {

    @Test
    @DisplayName("显示操作应将菜单标记为可见并转换状态")
    void show_shouldMarkVisibleAndTransitionState() {
        HiddenMenuState state = new HiddenMenuState();
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, false, true));
        domain.setState(state);

        state.show(domain);

        assertThat(domain.getAdvancedOptions().visible()).isTrue();
        assertThat(domain.getState()).isInstanceOf(VisibleMenuState.class);
    }

    @Test
    @DisplayName("隐藏操作在已隐藏状态下不应改变任何内容")
    void hide_whenAlreadyHidden_shouldDoNothing() {
        HiddenMenuState state = new HiddenMenuState();
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, false, true));
        domain.setState(state);

        state.hide(domain);

        assertThat(domain.getAdvancedOptions().visible()).isFalse();
        assertThat(domain.getState()).isInstanceOf(HiddenMenuState.class);
    }

    @Test
    @DisplayName("显示操作当 advancedOptions 为 null 时应只转换状态")
    void show_whenAdvancedOptionsNull_shouldOnlyTransitionState() {
        HiddenMenuState state = new HiddenMenuState();
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(null);
        domain.setState(state);

        state.show(domain);

        assertThat(domain.getState()).isInstanceOf(VisibleMenuState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        HiddenMenuState state = new HiddenMenuState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(MenuState.class);
    }
}
