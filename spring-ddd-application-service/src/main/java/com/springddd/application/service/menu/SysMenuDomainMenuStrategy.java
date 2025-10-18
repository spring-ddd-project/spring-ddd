package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.springframework.stereotype.Component;

@Component
public class SysMenuDomainMenuStrategy implements SysMenuDomainStrategy {

    @Override
    public boolean check(Integer type) {
        return 2 == type;
    }

    @Override
    public SysMenuDomain handle(Catalog catalog, Menu menu, Button button, MenuExtendInfo menuExtendInfo) {
        SysMenuDomain domain = new SysMenuDomain();

        Menu m = new Menu(menu.menuName(), menu.menuPath(), menu.component(), menu.affixTab(), menu.noBasicLayout(), menu.embedded());

        domain.setMenu(m);

        MenuExtendInfo extendInfo = new MenuExtendInfo(menuExtendInfo.order(), menuExtendInfo.title(), menuExtendInfo.icon(), menuExtendInfo.menuType(), menuExtendInfo.visible(), menuExtendInfo.menuStatus());
        domain.setMenuExtendInfo(extendInfo);
        return domain;
    }
}
