package com.springddd.domain.user.memento;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.ExtendInfo;
import lombok.Getter;

@Getter
public class SysUserMemento {
    private final Account account;
    private final ExtendInfo extendInfo;
    private final Long deptId;

    public SysUserMemento(Account account, ExtendInfo extendInfo, Long deptId) {
        this.account = account;
        this.extendInfo = extendInfo;
        this.deptId = deptId;
    }
}
























