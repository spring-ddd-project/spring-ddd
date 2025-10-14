package com.springddd.application.service.menu;

import com.springddd.domain.menu.MenuBasicInfo;
import com.springddd.domain.menu.MenuExtendInfo;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.menu.SysMenuDomainStrategy;
import org.springframework.stereotype.Component;

@Component
public class SysMenuDomainMenuStrategy implements SysMenuDomainStrategy {

    @Override
    public boolean check(Integer type) {
        return 2 == type;
    }

    @Override
    public SysMenuDomain handle(MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo) {
        SysMenuDomain domain = new SysMenuDomain();

        MenuBasicInfo basicInfo = new MenuBasicInfo(
                menuBasicInfo.menuName(),
                menuBasicInfo.menuPath(),
                menuBasicInfo.menuComponent(),
                null,
                menuBasicInfo.menuPermission());
        domain.setMenuBasicInfo(basicInfo);

        MenuExtendInfo extendInfo = new MenuExtendInfo(menuExtendInfo.order(), menuExtendInfo.title(), menuExtendInfo.affixTab(), menuExtendInfo.noBasicLayout(), menuExtendInfo.icon(), menuExtendInfo.menuType(), menuExtendInfo.visible(), menuExtendInfo.embedded(), menuExtendInfo.menuStatus());
        domain.setMenuExtendInfo(extendInfo);
        return domain;
    }
}
