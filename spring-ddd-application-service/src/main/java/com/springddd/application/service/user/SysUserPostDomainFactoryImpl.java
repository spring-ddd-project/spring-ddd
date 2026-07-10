package com.springddd.application.service.user;

import com.springddd.domain.user.*;
import org.springframework.stereotype.Component;

@Component
public class SysUserPostDomainFactoryImpl implements SysUserPostDomainFactory {

    @Override
    public SysUserPostDomain newInstance(UserPostInfo info) {
        SysUserPostDomain domain = new SysUserPostDomain();

        domain.setUserPostInfo(info);

        domain.setDeleteStatus(false);
        return domain;
    }
}
