package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import org.springframework.util.ObjectUtils;

public record RoleCode(String value) {

    public RoleCode {
        if (ObjectUtils.isEmpty(value)) {
            throw new RoleCodeNullException();
        }
    }
}
