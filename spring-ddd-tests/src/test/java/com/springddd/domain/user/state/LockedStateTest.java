package com.springddd.domain.user.state;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.Password;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LockedStateTest {

    @Test
    @DisplayName("锁定操作在已锁定状态下不应改变任何内容")
    void lock_whenAlreadyLocked_shouldDoNothing() {
        LockedState state = new LockedState();
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("testuser"), new Password("pass123"), "test@test.com", "13800138000", true));
        domain.setState(state);

        state.lock(domain);

        assertThat(domain.getAccount().lockStatus()).isTrue();
        assertThat(domain.getUserState()).isInstanceOf(LockedState.class);
    }

    @Test
    @DisplayName("解锁操作应将用户标记为正常并转换状态")
    void unlock_shouldMarkNormalAndTransitionState() {
        LockedState state = new LockedState();
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("testuser"), new Password("pass123"), "test@test.com", "13800138000", true));
        domain.setState(state);

        state.unlock(domain);

        assertThat(domain.getAccount().lockStatus()).isFalse();
        assertThat(domain.getUserState()).isInstanceOf(NormalState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        LockedState state = new LockedState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(UserState.class);
    }
}
