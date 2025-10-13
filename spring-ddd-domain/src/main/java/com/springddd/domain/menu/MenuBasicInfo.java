package com.springddd.domain.menu;

import com.springddd.domain.menu.exception.MenuNameNullException;
import org.springframework.util.ObjectUtils;

public record MenuBasicInfo(String menuName, String menuPath, String menuComponent, String api, String menuPermission) {

    public MenuBasicInfo {
        if (ObjectUtils.isEmpty(menuName)) {
            throw new MenuNameNullException();
        }
    }
}
