package com.springddd.domain.menu;

public interface SysMenuDomainFactory {

    SysMenuDomain create(MenuId parentId,
                         MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo,
                         Long deptId);
}
