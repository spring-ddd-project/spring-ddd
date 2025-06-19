package com.springddd.domain.role;

import com.springddd.domain.menu.MenuId;

public interface SysRoleMenuDomainFactory {

    SysRoleMenuDomain create(RoleId roleId, MenuId menuId, Long deptId, String createBy);
}
