package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuPathNullException;
import org.springframework.util.ObjectUtils;

public record MenuPath(String value) {

    public MenuPath {
        if (ObjectUtils.isEmpty(value)) {
            throw new MenuPathNullException();
        }
    }
}
