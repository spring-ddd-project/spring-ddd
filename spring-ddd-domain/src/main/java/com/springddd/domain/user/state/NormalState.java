package com.springddd.domain.user.state;

import com.springddd.domain.user.SysUserDomain;

public class NormalState implements UserState {
    @Override
    public void lock(SysUserDomain domain) {
        domain.getAccount().setLockStatus(true);
        domain.setState(new LockedState());
    }

    @Override
    public void unlock(SysUserDomain domain) {
        // Already normal
    }
}










































