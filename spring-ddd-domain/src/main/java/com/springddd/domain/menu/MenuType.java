package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuTypeNullException;
import org.springframework.util.ObjectUtils;

public record MenuType(String value) {

    public MenuType {
        if (ObjectUtils.isEmpty(value)) {
            throw new MenuTypeNullException();
        }
    }
}
