package com.springddd.domain.role;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.menu.MenuId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleMenuDomain extends AbstractDomainMask {

    private RoleMenuId roleMenuId;

    private RoleId roleId;

    private MenuId menuId;

    public void create() {}

    public void update(RoleId roleId, MenuId menuId, Long deptId, String updateBy) {
        this.roleId = roleId;
        this.menuId = menuId;
        super.setDeptId(deptId);
        super.setUpdateBy(updateBy);
        super.setUpdateTime(LocalDateTime.now());
    }

    public void delete(String updateBy) {
        super.setDeleteStatus("1");
    }
}
