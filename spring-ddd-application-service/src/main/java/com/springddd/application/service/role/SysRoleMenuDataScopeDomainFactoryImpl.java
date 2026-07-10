package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import org.springframework.stereotype.Component;

@Component
public class SysRoleMenuDataScopeDomainFactoryImpl implements SysRoleMenuDataScopeDomainFactory {

    @Override
    public SysRoleMenuDataScopeDomain newInstance(RoleMenuDataScopeInfo info) {
        SysRoleMenuDataScopeDomain domain = new SysRoleMenuDataScopeDomain();

        domain.setRoleMenuDataScopeInfo(info);

        domain.setDeleteStatus(false);
        return domain;
    }
}
