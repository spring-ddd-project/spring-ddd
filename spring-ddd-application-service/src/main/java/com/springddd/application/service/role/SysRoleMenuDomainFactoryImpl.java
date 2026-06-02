package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import com.springddd.domain.role.SysRoleMenuDomainFactory;
import org.springframework.stereotype.Component;


@Component
public class SysRoleMenuDomainFactoryImpl implements SysRoleMenuDomainFactory {
    @Override
    public SysRoleMenuDomain newInstance(RoleId roleId, MenuId menuId, Long deptId) {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleId(roleId);
        domain.setMenuId(menuId);

        domain.setDeptId(deptId);
        domain.setDeleteStatus(false);
        return domain;
    }
}
