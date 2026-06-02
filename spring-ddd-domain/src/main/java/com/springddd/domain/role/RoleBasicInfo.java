package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import com.springddd.domain.role.exception.RoleDataScopeNullException;
import com.springddd.domain.role.exception.RoleNameNullException;
import org.springframework.util.ObjectUtils;

public record RoleBasicInfo(String roleName, String roleCode, Integer roleDataScope, Boolean roleOwner) {

    public RoleBasicInfo {
        if (ObjectUtils.isEmpty(roleName)) {
            throw new RoleNameNullException();
        }
        if (ObjectUtils.isEmpty(roleCode)) {
            throw new RoleCodeNullException();
        }
        if (ObjectUtils.isEmpty(roleDataScope)) {
            throw new RoleDataScopeNullException();
        }
    }
}
