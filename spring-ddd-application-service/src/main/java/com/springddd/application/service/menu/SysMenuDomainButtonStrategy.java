package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.springframework.stereotype.Component;

@Component
public class SysMenuDomainButtonStrategy implements SysMenuDomainStrategy {

    @Override
    public boolean check(Integer type) {
        return 3 == type;
    }

    @Override
    public SysMenuDomain handle(Catalog catalog, Menu menu, Button button, MenuExtendInfo menuExtendInfo) {
        SysMenuDomain domain = new SysMenuDomain();

        Button bu = new Button(button.permission());
        domain.setButton(bu);

        MenuExtendInfo extendInfo = new MenuExtendInfo(menuExtendInfo.order(), menuExtendInfo.menuType(), menuExtendInfo.menuStatus());
        domain.setMenuExtendInfo(extendInfo);
        return domain;
    }
}
