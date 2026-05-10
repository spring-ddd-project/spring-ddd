package com.springddd.domain.user.state;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.SysUserDomain;

public class LockedState implements UserState {
    @Override
    public void lock(SysUserDomain domain) {
        // Already locked
    }

    @Override
    public void unlock(SysUserDomain domain) {
        Account old = domain.getAccount();
        domain.setAccount(new Account(old.username(), old.password(), old.email(), old.phone(), false));
        domain.setState(new NormalState());
    }
}





































































