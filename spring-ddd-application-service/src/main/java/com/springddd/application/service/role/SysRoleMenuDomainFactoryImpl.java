package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import com.springddd.domain.role.SysRoleMenuDomainFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SysRoleMenuDomainFactoryImpl implements SysRoleMenuDomainFactory {
    @Override
    public SysRoleMenuDomain newInstance(RoleId roleId, MenuId menuId, Long deptId, String createBy) {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleId(roleId);
        domain.setMenuId(menuId);

        LocalDateTime now = LocalDateTime.now();
        domain.setDeptId(deptId);
        domain.setDeleteStatus("0");
        domain.setCreateBy(createBy);
        domain.setCreateTime(now);
        domain.setUpdateBy(createBy);
        domain.setUpdateTime(now);
        return domain;
    }
}
