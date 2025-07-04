package com.springddd.domain.menu;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuBasicInfo {

    private MenuName menuName;

    private MenuPath menuPath;

    private MenuComponent menuComponent;

    private MenuRedirect menuRedirect;

    private MenuPermission menuPermission;

    public MenuBasicInfo(String menuName, String menuPath, String menuComponent, String menuRedirect, String menuPermission) {
        this.menuName = new MenuName(menuName);
        this.menuPath = new MenuPath(menuPath);
        this.menuComponent = new MenuComponent(menuComponent);
        this.menuRedirect = new MenuRedirect(menuRedirect);
        this.menuPermission = new MenuPermission(menuPermission);
    }

}
