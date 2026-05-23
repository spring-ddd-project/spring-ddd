package com.springddd.domain.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import com.springddd.domain.user.state.LockedState;
import com.springddd.domain.user.state.NormalState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SysUserDomainTest {

    private SysUserDomain createUserDomain() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        domain.setAccount(new Account(new Username("test"), new Password("pass"), "test@example.com", "13800138000", false));
        domain.setExtendInfo(new ExtendInfo("avatar.jpg", true));
        domain.setDeptId(1L);
        return domain;
    }

    @Test
    @DisplayName("create 应设置 NormalState")
    void create_shouldSetNormalState() {
        SysUserDomain domain = createUserDomain();
        domain.create();
        assertThat(domain.getUserState()).isInstanceOf(NormalState.class);
    }

    @Test
    @DisplayName("updateUser 应更新账户和部门")
    void updateUser_shouldUpdateAccountAndDept() {
        SysUserDomain domain = createUserDomain();
        Account newAccount = new Account(new Username("new"), new Password("newpass"), "new@example.com", "13900139000", true);
        ExtendInfo newExtendInfo = new ExtendInfo("new.jpg", false);

        domain.updateUser(newAccount, newExtendInfo, 2L);

        assertThat(domain.getAccount().username().value()).isEqualTo("new");
        assertThat(domain.getExtendInfo().avatar()).isEqualTo("new.jpg");
        assertThat(domain.getDeptId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("updateUser 传入 null deptId 应抛异常")
    void updateUser_withNullDeptId_shouldThrowException() {
        SysUserDomain domain = createUserDomain();
        assertThatThrownBy(() -> domain.updateUser(domain.getAccount(), domain.getExtendInfo(), null))
                .isInstanceOf(DeptIdNullException.class);
    }

    @Test
    @DisplayName("delete 应设置 deleteStatus 为 true")
    void delete_shouldSetDeleteStatus() {
        SysUserDomain domain = createUserDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    @DisplayName("restore 应设置 deleteStatus 为 false")
    void restore_shouldClearDeleteStatus() {
        SysUserDomain domain = createUserDomain();
        domain.delete();
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("lock 应设置 LockedState")
    void lock_shouldSetLockedState() {
        SysUserDomain domain = createUserDomain();
        domain.create();
        domain.lock();
        assertThat(domain.getUserState()).isInstanceOf(LockedState.class);
    }

    @Test
    @DisplayName("unlock 应设置 NormalState")
    void unlock_shouldSetNormalState() {
        SysUserDomain domain = createUserDomain();
        domain.create();
        domain.lock();
        domain.unlock();
        assertThat(domain.getUserState()).isInstanceOf(NormalState.class);
    }

    @Test
    @DisplayName("clone 应创建深拷贝")
    void clone_shouldCreateDeepCopy() {
        SysUserDomain original = createUserDomain();
        SysUserDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getUserId().value()).isEqualTo(original.getUserId().value());
        assertThat(cloned.getAccount()).isNotSameAs(original.getAccount());
    }

    @Test
    @DisplayName("saveToMemento 应保存当前状态")
    void saveToMemento_shouldSaveState() {
        SysUserDomain domain = createUserDomain();
        var memento = domain.saveToMemento();

        assertThat(memento.getAccount().username().value()).isEqualTo("test");
        assertThat(memento.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("restoreFromMemento 应恢复状态")
    void restoreFromMemento_shouldRestoreState() {
        SysUserDomain domain = createUserDomain();
        var memento = domain.saveToMemento();

        domain.setAccount(new Account(new Username("changed"), new Password("changed"), "c@example.com", "111", false));
        domain.restoreFromMemento(memento);

        assertThat(domain.getAccount().username().value()).isEqualTo("test");
    }

    @Test
    @DisplayName("lock 当 state 为 null 且 account.lockStatus 为 false 时应设置 LockedState")
    void lock_whenStateNullAndNotLocked_shouldSetLockedState() {
        SysUserDomain domain = createUserDomain();
        // userState is null because create() was not called
        // account.lockStatus is false
        domain.lock();
        assertThat(domain.getUserState()).isInstanceOf(LockedState.class);
        assertThat(domain.getAccount().lockStatus()).isTrue();
    }

    @Test
    @DisplayName("lock 当 state 为 null 且 account.lockStatus 为 true 时应保持 LockedState")
    void lock_whenStateNullAndAlreadyLocked_shouldKeepLockedState() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        domain.setAccount(new Account(new Username("test"), new Password("pass"), "test@example.com", "13800138000", true));
        domain.setExtendInfo(new ExtendInfo("avatar.jpg", true));
        domain.setDeptId(1L);
        domain.lock();
        assertThat(domain.getUserState()).isInstanceOf(LockedState.class);
        assertThat(domain.getAccount().lockStatus()).isTrue();
    }

    @Test
    @DisplayName("unlock 当 state 为 null 且 account.lockStatus 为 false 时应保持 NormalState")
    void unlock_whenStateNullAndNotLocked_shouldKeepNormalState() {
        SysUserDomain domain = createUserDomain();
        // userState is null, account.lockStatus is false
        domain.unlock();
        assertThat(domain.getUserState()).isInstanceOf(NormalState.class);
        assertThat(domain.getAccount().lockStatus()).isFalse();
    }

    @Test
    @DisplayName("unlock 当 state 为 null 且 account.lockStatus 为 true 时应设置 NormalState")
    void unlock_whenStateNullAndLocked_shouldSetNormalState() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        domain.setAccount(new Account(new Username("test"), new Password("pass"), "test@example.com", "13800138000", true));
        domain.setExtendInfo(new ExtendInfo("avatar.jpg", true));
        domain.setDeptId(1L);
        domain.unlock();
        assertThat(domain.getUserState()).isInstanceOf(NormalState.class);
        assertThat(domain.getAccount().lockStatus()).isFalse();
    }

    @Test
    @DisplayName("updateUserStatus 传入 true 应锁定用户")
    void updateUserStatus_withTrue_shouldLockUser() {
        SysUserDomain domain = createUserDomain();
        domain.updateUserStatus(true);
        assertThat(domain.getUserState()).isInstanceOf(LockedState.class);
        assertThat(domain.getAccount().lockStatus()).isTrue();
    }

    @Test
    @DisplayName("updateUserStatus 传入 false 应解锁用户")
    void updateUserStatus_withFalse_shouldUnlockUser() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("test"), new Password("pass"), "test@example.com", "13800138000", true));
        domain.updateUserStatus(false);
        assertThat(domain.getUserState()).isInstanceOf(NormalState.class);
        assertThat(domain.getAccount().lockStatus()).isFalse();
    }

    @Test
    @DisplayName("clone 当字段为 null 时应正确处理")
    void clone_withNullFields_shouldHandleNulls() {
        SysUserDomain original = new SysUserDomain();
        original.setUserId(null);
        original.setAccount(null);
        original.setExtendInfo(null);
        original.setDeptId(1L);

        SysUserDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getUserId()).isNull();
        assertThat(cloned.getAccount()).isNull();
        assertThat(cloned.getExtendInfo()).isNull();
        assertThat(cloned.getDeptId()).isEqualTo(original.getDeptId());
    }
}
