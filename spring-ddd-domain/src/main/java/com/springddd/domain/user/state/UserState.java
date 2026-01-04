package com.springddd.domain.user.state;

import com.springddd.domain.user.SysUserDomain;

public interface UserState {
    void lock(SysUserDomain domain);
    void unlock(SysUserDomain domain);
}























