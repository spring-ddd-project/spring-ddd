package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleMenuDataScopeMenuIdNullException;
import com.springddd.domain.role.exception.RoleMenuDataScopeNullException;
import com.springddd.domain.role.exception.RoleMenuDataScopeRoleIdNullException;
import org.springframework.util.ObjectUtils;

public record RoleMenuDataScopeInfo(Long roleId, Long menuId, Integer dataScope) {

    public RoleMenuDataScopeInfo {
        if (ObjectUtils.isEmpty(roleId)) {
            throw new RoleMenuDataScopeRoleIdNullException();
        }
        if (ObjectUtils.isEmpty(menuId)) {
            throw new RoleMenuDataScopeMenuIdNullException();
        }
        if (ObjectUtils.isEmpty(dataScope)) {
            throw new RoleMenuDataScopeNullException();
        }
        if (!DataScope.isValid(dataScope)) {
            throw new RoleMenuDataScopeNullException();
        }
    }
}
