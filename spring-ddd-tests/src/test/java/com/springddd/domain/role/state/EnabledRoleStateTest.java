package com.springddd.domain.role.state;

import com.springddd.domain.role.RoleBasicInfo;
import com.springddd.domain.role.SysRoleDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnabledRoleStateTest {

    @Test
    @DisplayName("启用操作在已启用状态下不应改变任何内容")
    void enable_whenAlreadyEnabled_shouldDoNothing() {
        EnabledRoleState state = new EnabledRoleState();
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("ADMIN", "admin", 1, true, 1, false));
        domain.setState(state);

        state.enable(domain);

        assertThat(domain.getRoleBasicInfo().roleStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(EnabledRoleState.class);
    }

    @Test
    @DisplayName("禁用操作应将角色标记为禁用并转换状态")
    void disable_shouldMarkDisabledAndTransitionState() {
        EnabledRoleState state = new EnabledRoleState();
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("USER", "user", 1, true, 1, false));
        domain.setState(state);

        state.disable(domain);

        assertThat(domain.getRoleBasicInfo().roleStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(DisabledRoleState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        EnabledRoleState state = new EnabledRoleState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(RoleState.class);
    }
}
