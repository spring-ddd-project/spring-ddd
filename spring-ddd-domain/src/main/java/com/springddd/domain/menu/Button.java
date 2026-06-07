package com.springddd.domain.menu;

import org.springframework.util.ObjectUtils;

public record Button(String permission, String api) {

    public Button(String permission) {
        this(permission, null);
    }

    public Button {
        if (ObjectUtils.isEmpty(permission)) {
            throw new IllegalArgumentException("permission cannot be null");
        }
    }
}
