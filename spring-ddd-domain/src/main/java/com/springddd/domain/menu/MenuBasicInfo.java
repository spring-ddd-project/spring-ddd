package com.springddd.domain.menu;

import lombok.Data;

@Data
public class MenuBasicInfo {

    private MenuName menuName;

    private MenuPermission menuPermission;

    private MenuType menuType;

    private MenuIcon menuIcon;

    private MenuPath menuPath;

    private MenuComponent menuComponent;

}
