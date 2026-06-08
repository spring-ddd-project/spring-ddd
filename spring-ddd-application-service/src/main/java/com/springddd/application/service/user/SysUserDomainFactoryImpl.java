package com.springddd.application.service.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import com.springddd.domain.user.Account;
import com.springddd.domain.user.ExtendInfo;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.SysUserDomainFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class SysUserDomainFactoryImpl implements SysUserDomainFactory {

    @Override
    public SysUserDomain newInstance(Account account, ExtendInfo extendInfo, Long deptId) {
        if (ObjectUtils.isEmpty(deptId)) {
            throw new DeptIdNullException();
        }
        SysUserDomain sysUserDomain = new SysUserDomain();
        sysUserDomain.setAccount(account);
        sysUserDomain.setExtendInfo(extendInfo);

        sysUserDomain.setDeptId(deptId);
        sysUserDomain.setDeleteStatus(false);
        return sysUserDomain;
    }
}
