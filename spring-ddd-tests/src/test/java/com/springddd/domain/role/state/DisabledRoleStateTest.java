package com.springddd.domain.role.state;

import com.springddd.domain.role.RoleBasicInfo;
import com.springddd.domain.role.SysRoleDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DisabledRoleStateTest {

    @Test
    @DisplayName("启用操作应将角色标记为启用并转换状态")
    void enable_shouldMarkEnabledAndTransitionState() {
        DisabledRoleState state = new DisabledRoleState();
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("USER", "user", 1, false, 1, false));
        domain.setState(state);

        state.enable(domain);

        assertThat(domain.getRoleBasicInfo().roleStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(EnabledRoleState.class);
    }

    @Test
    @DisplayName("禁用操作在已禁用状态下不应改变任何内容")
    void disable_whenAlreadyDisabled_shouldDoNothing() {
        DisabledRoleState state = new DisabledRoleState();
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleBasicInfo(new RoleBasicInfo("ADMIN", "admin", 1, false, 1, false));
        domain.setState(state);

        state.disable(domain);

        assertThat(domain.getRoleBasicInfo().roleStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(DisabledRoleState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        DisabledRoleState state = new DisabledRoleState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(RoleState.class);
    }
}
