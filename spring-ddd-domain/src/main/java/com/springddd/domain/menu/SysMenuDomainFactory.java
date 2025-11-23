package com.springddd.domain.menu;

public interface SysMenuDomainFactory {

    SysMenuDomain create(MenuId parentId, String name, Catalog catalog, Menu menu, Button button,
                         MenuExtendInfo menuExtendInfo,
                         Long deptId);
}


















