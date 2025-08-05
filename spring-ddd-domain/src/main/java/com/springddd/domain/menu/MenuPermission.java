package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuPermissionNullException;
import org.springframework.util.ObjectUtils;

public record MenuPermission(String value) {

    public MenuPermission {
        if (ObjectUtils.isEmpty(value)) {
            throw new MenuPermissionNullException();
        }
    }
}
