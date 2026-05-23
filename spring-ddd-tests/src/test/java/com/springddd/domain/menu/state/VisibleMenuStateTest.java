package com.springddd.domain.menu.state;

import com.springddd.domain.menu.AdvancedOptions;
import com.springddd.domain.menu.SysMenuDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VisibleMenuStateTest {

    @Test
    @DisplayName("显示操作在已可见状态下不应改变任何内容")
    void show_whenAlreadyVisible_shouldDoNothing() {
        VisibleMenuState state = new VisibleMenuState();
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, true, true));
        domain.setState(state);

        state.show(domain);

        assertThat(domain.getAdvancedOptions().visible()).isTrue();
        assertThat(domain.getState()).isInstanceOf(VisibleMenuState.class);
    }

    @Test
    @DisplayName("隐藏操作应将菜单标记为隐藏并转换状态")
    void hide_shouldMarkHiddenAndTransitionState() {
        VisibleMenuState state = new VisibleMenuState();
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, true, true));
        domain.setState(state);

        state.hide(domain);

        assertThat(domain.getAdvancedOptions().visible()).isFalse();
        assertThat(domain.getState()).isInstanceOf(HiddenMenuState.class);
    }

    @Test
    @DisplayName("隐藏操作当 advancedOptions 为 null 时应只转换状态")
    void hide_whenAdvancedOptionsNull_shouldOnlyTransitionState() {
        VisibleMenuState state = new VisibleMenuState();
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(null);
        domain.setState(state);

        state.hide(domain);

        assertThat(domain.getState()).isInstanceOf(HiddenMenuState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        VisibleMenuState state = new VisibleMenuState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(MenuState.class);
    }
}
