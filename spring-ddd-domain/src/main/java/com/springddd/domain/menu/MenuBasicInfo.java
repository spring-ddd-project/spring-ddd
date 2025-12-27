package com.springddd.domain.menu;

import lombok.Data;

@Data
public class MenuBasicInfo {

    private MenuName menuName;

    private MenuPath menuPath;

    private MenuComponent menuComponent;

    private MenuRedirect menuRedirect;

    private MenuPermission menuPermission;

}
