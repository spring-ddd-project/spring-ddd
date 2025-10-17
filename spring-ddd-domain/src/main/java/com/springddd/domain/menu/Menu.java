package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuNameNullException;
import org.springframework.util.ObjectUtils;

public record Menu(String menuName, String menuPath, String component, Boolean affixTab, Boolean noBasicLayout, Boolean embedded) {

    public Menu {
        if (ObjectUtils.isEmpty(menuName)) {
            throw new MenuNameNullException();
        }
    }
}
