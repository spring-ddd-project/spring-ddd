package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuComponentNullException;
import com.springddd.domain.menu.exception.MenuPathNullException;
import org.springframework.util.ObjectUtils;

public record Menu(String menuPath, String component, Boolean affixTab, Boolean noBasicLayout, Boolean embedded) {

    public Menu {
        if (ObjectUtils.isEmpty(menuPath)) {
            throw new MenuPathNullException();
        }
        if (ObjectUtils.isEmpty(component)) {
            throw new MenuComponentNullException();
        }
    }
}
