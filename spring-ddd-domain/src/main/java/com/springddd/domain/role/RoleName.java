package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleNameNullException;
import org.springframework.util.ObjectUtils;

public record RoleName(String value) {

    public RoleName {
        if (ObjectUtils.isEmpty(value)) {
            throw new RoleNameNullException();
        }
    }
}
