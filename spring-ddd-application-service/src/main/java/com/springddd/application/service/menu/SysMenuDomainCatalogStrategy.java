package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import org.springframework.stereotype.Component;

@Component
public class SysMenuDomainCatalogStrategy implements SysMenuDomainStrategy {

    @Override
    public boolean check(Integer type) {
        return 1 == type;
    }

    @Override
    public SysMenuDomain handle(Catalog catalog, Menu menu, Button button, MenuExtendInfo menuExtendInfo) {
        SysMenuDomain domain = new SysMenuDomain();

        Catalog ca = new Catalog(catalog.menuRedirect());
        domain.setCatalog(ca);

        MenuExtendInfo extendInfo = new MenuExtendInfo(menuExtendInfo.order(), menuExtendInfo.title(), menuExtendInfo.menuType(), menuExtendInfo.icon(), menuExtendInfo.menuStatus(), menuExtendInfo.visible());
        domain.setMenuExtendInfo(extendInfo);
        return domain;
    }
}
