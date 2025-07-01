package com.springddd.application.service.menu;

import com.springddd.domain.menu.MenuBasicInfo;
import com.springddd.domain.menu.MenuExtendInfo;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.menu.SysMenuDomainStrategy;
import org.springframework.stereotype.Component;

@Component
public class SysMenuDomainButtonStrategy implements SysMenuDomainStrategy {

    @Override
    public boolean check(Integer type) {
        return 3 == type;
    }

    @Override
    public SysMenuDomain handle(MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo) {
        SysMenuDomain domain = new SysMenuDomain();

        MenuBasicInfo basicInfo = new MenuBasicInfo();
        basicInfo.setMenuName(menuBasicInfo.getMenuName());
        basicInfo.setMenuPermission(menuBasicInfo.getMenuPermission());
        domain.setMenuBasicInfo(basicInfo);

        MenuExtendInfo extendInfo = new MenuExtendInfo();
        extendInfo.setOrder(menuExtendInfo.getOrder());
        extendInfo.setTitle(menuExtendInfo.getTitle());
        extendInfo.setMenuType(menuExtendInfo.getMenuType());
        extendInfo.setMenuStatus(menuExtendInfo.getMenuStatus());
        domain.setMenuExtendInfo(extendInfo);
        return domain;
    }
}
