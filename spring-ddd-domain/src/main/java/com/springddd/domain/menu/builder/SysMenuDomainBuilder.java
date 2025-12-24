package com.springddd.domain.menu.builder;

import com.springddd.domain.menu.*;

public class SysMenuDomainBuilder {
    private final SysMenuDomain domain = new SysMenuDomain();

    public SysMenuDomainBuilder menuId(Long id) {
        if (id != null) domain.setMenuId(new MenuId(id));
        return this;
    }

    public SysMenuDomainBuilder parentId(Long parentId) {
        if (parentId != null) domain.setParentId(new MenuId(parentId));
        return this;
    }

    public SysMenuDomainBuilder name(String name) {
        domain.setName(name);
        return this;
    }

    public SysMenuDomainBuilder catalog(String routePath, String component, String redirect) {
        domain.setCatalog(new Catalog(routePath, component, redirect));
        return this;
    }

    public SysMenuDomainBuilder menu(String menuPath, String component, Boolean affixTab, Boolean noBasicLayout, Boolean embedded) {
        domain.setMenu(new Menu(menuPath, component, affixTab, noBasicLayout, embedded));
        return this;
    }

    public SysMenuDomainBuilder button(String authCode) {
        domain.setButton(new Button(authCode));
        return this;
    }

    public SysMenuDomainBuilder advancedOptions(Integer order, String icon, Integer menuType, Boolean visible, Boolean menuStatus) {
        domain.setAdvancedOptions(new AdvancedOptions(order, icon, menuType, visible, menuStatus));
        return this;
    }

    public SysMenuDomainBuilder menuExtendInfo(Long creatorId) {
        domain.setMenuExtendInfo(new MenuExtendInfo(creatorId));
        return this;
    }

    public SysMenuDomainBuilder deptId(Long deptId) {
        domain.setDeptId(deptId);
        return this;
    }

    public SysMenuDomain build() {
        domain.create();
        return domain;
    }
}


















