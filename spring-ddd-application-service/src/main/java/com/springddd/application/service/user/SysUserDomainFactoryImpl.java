package com.springddd.application.service.user;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.ExtendInfo;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.SysUserDomainFactory;
import org.springframework.stereotype.Component;

@Component
public class SysUserDomainFactoryImpl implements SysUserDomainFactory {

    @Override
    public SysUserDomain newInstance(Account account, ExtendInfo extendInfo, Long deptId) {
        SysUserDomain sysUserDomain = new SysUserDomain();
        sysUserDomain.setAccount(account);
        sysUserDomain.setExtendInfo(extendInfo);

        sysUserDomain.setDeptId(deptId);
        sysUserDomain.setDeleteStatus("0");
        return sysUserDomain;
    }
}
