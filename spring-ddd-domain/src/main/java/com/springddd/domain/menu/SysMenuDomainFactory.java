package com.springddd.domain.menu;

public interface SysMenuDomainFactory {

    SysMenuDomain create(MenuId parentId, Catalog catalog,
                         MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo,
                         Long deptId);
}
