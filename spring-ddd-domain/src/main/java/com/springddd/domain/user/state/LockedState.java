package com.springddd.domain.user.state;

import com.springddd.domain.user.SysUserDomain;

public class LockedState implements UserState {
    @Override
    public void lock(SysUserDomain domain) {
        // Already locked
    }

    @Override
    public void unlock(SysUserDomain domain) {
        domain.getAccount().setLockStatus(false);
        domain.setState(new NormalState());
    }
}


