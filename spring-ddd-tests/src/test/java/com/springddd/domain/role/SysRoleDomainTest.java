package com.springddd.domain.role;

import com.springddd.domain.role.state.DisabledRoleState;
import com.springddd.domain.role.state.EnabledRoleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.springddd.domain.role.observer.RoleObserver;
import org.mockito.Mockito;

class SysRoleDomainTest {

    private SysRoleDomain createRoleDomain() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        domain.setRoleBasicInfo(new RoleBasicInfo("admin", "ADMIN", 1, true, 1, true));
        domain.setRoleExtendInfo(new RoleExtendInfo("管理员", true, "系统管理员", true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    @DisplayName("enable 应设置 EnabledRoleState")
    void enable_shouldSetEnabledState() {
        SysRoleDomain domain = createRoleDomain();
        domain.enable();
        assertThat(domain.getState()).isInstanceOf(EnabledRoleState.class);
    }

    @Test
    @DisplayName("disable 应设置 DisabledRoleState")
    void disable_shouldSetDisabledState() {
        SysRoleDomain domain = createRoleDomain();
        domain.disable();
        assertThat(domain.getState()).isInstanceOf(DisabledRoleState.class);
    }

    @Test
    @DisplayName("updateRole 应更新角色信息")
    void updateRole_shouldUpdateInfo() {
        SysRoleDomain domain = createRoleDomain();
        RoleBasicInfo newBasic = new RoleBasicInfo("user", "USER", 2, false, 2, false);
        RoleExtendInfo newExtend = new RoleExtendInfo("普通用户", false, "普通用户角色", false);

        domain.updateRole(newBasic, newExtend, null, 2L);

        assertThat(domain.getRoleBasicInfo().roleName()).isEqualTo("user");
        assertThat(domain.getDeptId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("delete 应设置 deleteStatus")
    void delete_shouldSetDeleteStatus() {
        SysRoleDomain domain = createRoleDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    @DisplayName("restore 应清除 deleteStatus")
    void restore_shouldClearDeleteStatus() {
        SysRoleDomain domain = createRoleDomain();
        domain.delete();
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("clone 应创建深拷贝")
    void clone_shouldCreateDeepCopy() {
        SysRoleDomain original = createRoleDomain();
        SysRoleDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getRoleId().value()).isEqualTo(1L);
        assertThat(cloned.getRoleBasicInfo()).isNotSameAs(original.getRoleBasicInfo());
    }

    @Test
    @DisplayName("enable 当 state 为 null 且 roleStatus 为 true 时应保持 EnabledRoleState")
    void enable_whenStateNullAndRoleStatusTrue_shouldSetEnabledState() {
        SysRoleDomain domain = createRoleDomain();
        // state is null, roleBasicInfo.roleStatus() is true
        domain.enable();
        assertThat(domain.getState()).isInstanceOf(EnabledRoleState.class);
        assertThat(domain.getRoleBasicInfo().roleStatus()).isTrue();
    }

    @Test
    @DisplayName("enable 当 state 为 null 且 roleStatus 为 false 时应启用角色")
    void enable_whenStateNullAndRoleStatusFalse_shouldEnableRole() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        domain.setRoleBasicInfo(new RoleBasicInfo("user", "USER", 1, false, 1, true));
        domain.setRoleExtendInfo(new RoleExtendInfo("普通用户", false, "普通用户角色", false));
        domain.setDeptId(1L);
        domain.enable();
        assertThat(domain.getState()).isInstanceOf(EnabledRoleState.class);
        assertThat(domain.getRoleBasicInfo().roleStatus()).isTrue();
    }

    @Test
    @DisplayName("disable 当 state 为 null 且 roleStatus 为 true 时应禁用角色")
    void disable_whenStateNullAndRoleStatusTrue_shouldDisableRole() {
        SysRoleDomain domain = createRoleDomain();
        // state is null, roleBasicInfo.roleStatus() is true
        domain.disable();
        assertThat(domain.getState()).isInstanceOf(DisabledRoleState.class);
        assertThat(domain.getRoleBasicInfo().roleStatus()).isFalse();
    }

    @Test
    @DisplayName("disable 当 state 为 null 且 roleStatus 为 false 时应保持 DisabledRoleState")
    void disable_whenStateNullAndRoleStatusFalse_shouldKeepDisabledState() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        domain.setRoleBasicInfo(new RoleBasicInfo("user", "USER", 1, false, 1, true));
        domain.setRoleExtendInfo(new RoleExtendInfo("普通用户", false, "普通用户角色", false));
        domain.setDeptId(1L);
        domain.disable();
        assertThat(domain.getState()).isInstanceOf(DisabledRoleState.class);
        assertThat(domain.getRoleBasicInfo().roleStatus()).isFalse();
    }

    @Test
    @DisplayName("addObserver 应添加观察者并在 updateRole 时通知")
    void addObserver_shouldAddAndNotifyOnUpdate() {
        SysRoleDomain domain = createRoleDomain();
        RoleObserver observer = Mockito.mock(RoleObserver.class);
        domain.addObserver(observer);

        RoleBasicInfo newBasic = new RoleBasicInfo("updated", "UPDATED", 3, true, 1, true);
        RoleExtendInfo newExtend = new RoleExtendInfo("updated", true, "updated desc", true);

        domain.updateRole(newBasic, newExtend, null, 2L);

        verify(observer).onUpdate(domain);
    }

    @Test
    @DisplayName("updateRole 应通知多个观察者")
    void updateRole_shouldNotifyMultipleObservers() {
        SysRoleDomain domain = createRoleDomain();
        RoleObserver observer1 = Mockito.mock(RoleObserver.class);
        RoleObserver observer2 = Mockito.mock(RoleObserver.class);
        domain.addObserver(observer1);
        domain.addObserver(observer2);

        RoleBasicInfo newBasic = new RoleBasicInfo("updated", "UPDATED", 3, true, 1, true);
        RoleExtendInfo newExtend = new RoleExtendInfo("updated", true, "updated desc", true);

        domain.updateRole(newBasic, newExtend, null, 2L);

        verify(observer1).onUpdate(domain);
        verify(observer2).onUpdate(domain);
    }

    @Test
    @DisplayName("clone 当字段为 null 时应正确处理")
    void clone_withNullFields_shouldHandleNulls() {
        SysRoleDomain original = new SysRoleDomain();
        original.setRoleId(null);
        original.setRoleBasicInfo(null);
        original.setRoleExtendInfo(null);
        original.setDataPermission(null);
        original.setDeptId(1L);

        SysRoleDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getRoleId()).isNull();
        assertThat(cloned.getRoleBasicInfo()).isNull();
        assertThat(cloned.getRoleExtendInfo()).isNull();
        assertThat(cloned.getDataPermission()).isNull();
        assertThat(cloned.getDeptId()).isEqualTo(original.getDeptId());
    }

    @Test
    @DisplayName("clone 当所有字段非空时应创建完整深拷贝")
    void clone_withAllFieldsNonNull_shouldCreateCompleteDeepCopy() {
        SysRoleDomain original = createRoleDomain();
        RowScope rowScope = new RowScope(List.of(1L), List.of(2L), List.of(3L), true);
        ColumnRule columnRule = new ColumnRule("code", "name", List.of("col"), "type", List.of(1L));
        original.setDataPermission(new DataPermission(rowScope, List.of(columnRule), 1, List.of(1L)));

        SysRoleDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getRoleId().value()).isEqualTo(1L);
        assertThat(cloned.getRoleBasicInfo()).isNotSameAs(original.getRoleBasicInfo());
        assertThat(cloned.getRoleExtendInfo()).isNotSameAs(original.getRoleExtendInfo());
        assertThat(cloned.getDataPermission()).isNotSameAs(original.getDataPermission());
        assertThat(cloned.getDataPermission().dataScope()).isEqualTo(original.getDataPermission().dataScope());
    }

    @Test
    @DisplayName("clone 当 roleId 为 null 时应正确处理")
    void clone_withNullRoleId_shouldHandleNull() {
        SysRoleDomain original = createRoleDomain();
        original.setRoleId(null);

        SysRoleDomain cloned = original.clone();

        assertThat(cloned.getRoleId()).isNull();
        assertThat(cloned.getRoleBasicInfo()).isNotNull();
    }

    @Test
    @DisplayName("clone 当 roleBasicInfo 为 null 时应正确处理")
    void clone_withNullRoleBasicInfo_shouldHandleNull() {
        SysRoleDomain original = createRoleDomain();
        original.setRoleBasicInfo(null);

        SysRoleDomain cloned = original.clone();

        assertThat(cloned.getRoleBasicInfo()).isNull();
        assertThat(cloned.getRoleId()).isNotNull();
    }

    @Test
    @DisplayName("clone 当 roleExtendInfo 为 null 时应正确处理")
    void clone_withNullRoleExtendInfo_shouldHandleNull() {
        SysRoleDomain original = createRoleDomain();
        original.setRoleExtendInfo(null);

        SysRoleDomain cloned = original.clone();

        assertThat(cloned.getRoleExtendInfo()).isNull();
        assertThat(cloned.getRoleBasicInfo()).isNotNull();
    }
}
