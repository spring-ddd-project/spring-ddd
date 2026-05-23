package com.springddd.domain.user.state;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.Password;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NormalStateTest {

    @Test
    @DisplayName("锁定操作应将用户标记为锁定并转换状态")
    void lock_shouldMarkLockedAndTransitionState() {
        NormalState state = new NormalState();
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("testuser"), new Password("pass123"), "test@test.com", "13800138000", false));
        domain.setState(state);

        state.lock(domain);

        assertThat(domain.getAccount().lockStatus()).isTrue();
        assertThat(domain.getUserState()).isInstanceOf(LockedState.class);
    }

    @Test
    @DisplayName("解锁操作在正常状态下不应改变任何内容")
    void unlock_whenAlreadyNormal_shouldDoNothing() {
        NormalState state = new NormalState();
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("testuser"), new Password("pass123"), "test@test.com", "13800138000", false));
        domain.setState(state);

        state.unlock(domain);

        assertThat(domain.getAccount().lockStatus()).isFalse();
        assertThat(domain.getUserState()).isInstanceOf(NormalState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        NormalState state = new NormalState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(UserState.class);
    }
}
