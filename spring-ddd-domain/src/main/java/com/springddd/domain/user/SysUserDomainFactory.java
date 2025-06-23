package com.springddd.domain.user;

public interface SysUserDomainFactory {

    SysUserDomain newInstance(Account account, ExtendInfo extendInfo,
                              Long deptId);
}
