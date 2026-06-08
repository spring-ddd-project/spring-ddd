package com.springddd.domain.role;

import com.springddd.domain.menu.MenuId;

public interface SysRoleMenuDomainFactory {

    SysRoleMenuDomain newInstance(RoleId roleId, MenuId menuId, Long deptId);
}
