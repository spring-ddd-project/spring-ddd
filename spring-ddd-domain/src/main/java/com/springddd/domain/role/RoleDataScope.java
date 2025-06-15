package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleDataScopeNullException;
import org.springframework.util.ObjectUtils;

public record RoleDataScope(String value) {

    public RoleDataScope {
        if (ObjectUtils.isEmpty(value)) {
            throw new RoleDataScopeNullException();
        }
    }
}
