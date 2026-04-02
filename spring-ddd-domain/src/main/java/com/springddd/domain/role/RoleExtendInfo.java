package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleStatusNullException;
import org.springframework.util.ObjectUtils;

public record RoleExtendInfo(String roleDesc, Boolean roleStatus) {

    public RoleExtendInfo {
        if (ObjectUtils.isEmpty(roleStatus)) {
            throw new RoleStatusNullException();
        }
    }
}









































