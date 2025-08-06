package com.springddd.domain.role;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.menu.MenuId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleMenuDomain extends AbstractDomainMask {

    private RoleMenuId roleMenuId;

    private RoleId roleId;

    private MenuId menuId;
}
