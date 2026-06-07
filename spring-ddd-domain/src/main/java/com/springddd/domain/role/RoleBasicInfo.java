package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import com.springddd.domain.role.exception.RoleNameNullException;
import org.springframework.util.ObjectUtils;

public record RoleBasicInfo(String roleName, String roleCode, Integer roleSort, Boolean roleStatus, Boolean roleOwner) {

    public RoleBasicInfo(String roleName, String roleCode, Boolean roleOwner) {
        this(roleName, roleCode, null, null, roleOwner);
        if (ObjectUtils.isEmpty(roleName)) {
            throw new RoleNameNullException();
        }
        if (ObjectUtils.isEmpty(roleCode)) {
            throw new RoleCodeNullException();
        }
    }
}
