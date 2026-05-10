package com.springddd.domain.user.state;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.SysUserDomain;

public class NormalState implements UserState {
    @Override
    public void lock(SysUserDomain domain) {
        Account old = domain.getAccount();
        domain.setAccount(new Account(old.username(), old.password(), old.email(), old.phone(), true));
        domain.setState(new LockedState());
    }

    @Override
    public void unlock(SysUserDomain domain) {
        // Already normal
    }
}

















































