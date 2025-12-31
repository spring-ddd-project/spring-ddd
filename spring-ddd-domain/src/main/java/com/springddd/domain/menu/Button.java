package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuPermissionNullException;
import org.springframework.util.ObjectUtils;

public record Button(String permission, String api) {

    public Button {
        if (ObjectUtils.isEmpty(permission)) {
            throw new MenuPermissionNullException();
        }
    }
}























