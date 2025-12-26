package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.springframework.stereotype.Component;

@Component
public class SysMenuDomainFactoryImpl implements SysMenuDomainFactory {
    @Override
    public SysMenuDomain create(MenuId parentId, MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo, Long deptId) {
        SysMenuDomain domain = new SysMenuDomain();

        domain.setParentId(parentId);
        domain.setMenuBasicInfo(menuBasicInfo);
        domain.setMenuExtendInfo(menuExtendInfo);

        domain.setDeptId(deptId);
        domain.setDeleteStatus(false);
        return domain;
    }
}
