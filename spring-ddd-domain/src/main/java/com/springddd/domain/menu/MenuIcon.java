package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuIconNullException;
import org.springframework.util.ObjectUtils;

public record MenuIcon(String value) {

    public MenuIcon {
        if (ObjectUtils.isEmpty(value)) {
            throw new MenuIconNullException();
        }
    }
}
