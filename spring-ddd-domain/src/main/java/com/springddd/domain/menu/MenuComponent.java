package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuComponentNullException;
import org.springframework.util.ObjectUtils;

public record MenuComponent(String value) {

    public MenuComponent {
        if (ObjectUtils.isEmpty(value)) {
            throw new MenuComponentNullException();
        }
    }
}
