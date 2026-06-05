package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleStatusNullException;
import org.springframework.util.ObjectUtils;

public record RoleExtendInfo(String roleRemark, Boolean ownerStatus, String roleDesc, Boolean roleStatus) {

    public RoleExtendInfo(String roleDesc, Boolean roleStatus) {
        this(null, null, roleDesc, roleStatus);
        if (ObjectUtils.isEmpty(roleStatus)) {
            throw new RoleStatusNullException();
        }
    }
}
