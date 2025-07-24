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
    public SysMenuDomain handle(MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo) {
        SysMenuDomain domain = new SysMenuDomain();

        MenuBasicInfo basicInfo = new MenuBasicInfo(
                menuBasicInfo.menuName(),
                menuBasicInfo.menuPath(),
                null,
                null,
                menuBasicInfo.menuRedirect(),
                null);
        domain.setMenuBasicInfo(basicInfo);

        MenuExtendInfo extendInfo = new MenuExtendInfo(menuExtendInfo.order(), menuExtendInfo.title(), menuExtendInfo.menuType(), menuExtendInfo.icon(), menuExtendInfo.menuStatus(), menuExtendInfo.visible());
        domain.setMenuExtendInfo(extendInfo);
        return domain;
    }
}
