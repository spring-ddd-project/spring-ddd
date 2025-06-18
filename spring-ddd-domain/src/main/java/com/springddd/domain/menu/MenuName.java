package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuNameNullException;
import org.springframework.util.ObjectUtils;

public record MenuName(String value) {

    public MenuName {
        if (ObjectUtils.isEmpty(value)) {
            throw new MenuNameNullException();
        }
    }
}
