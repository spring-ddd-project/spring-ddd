package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SysMenuDomainFactoryImpl implements SysMenuDomainFactory {
    @Override
    public SysMenuDomain create(MenuId parentId, MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo, Long deptId, String createBy) {
        SysMenuDomain domain = new SysMenuDomain();

        domain.setParentId(parentId);
        domain.setMenuBasicInfo(menuBasicInfo);
        domain.setMenuExtendInfo(menuExtendInfo);

        LocalDateTime now = LocalDateTime.now();
        domain.setDeptId(deptId);
        domain.setDeleteStatus("0");
        domain.setCreateBy(createBy);
        domain.setUpdateTime(now);
        domain.setCreateTime(now);
        domain.setUpdateBy(createBy);
        return domain;
    }
}
