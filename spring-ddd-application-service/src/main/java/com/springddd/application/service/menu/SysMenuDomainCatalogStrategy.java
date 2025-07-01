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

        MenuBasicInfo basicInfo = new MenuBasicInfo();
        basicInfo.setMenuName(menuBasicInfo.getMenuName());
        basicInfo.setMenuPath(menuBasicInfo.getMenuPath());
        basicInfo.setMenuRedirect(menuBasicInfo.getMenuRedirect());
        domain.setMenuBasicInfo(basicInfo);

        MenuExtendInfo extendInfo = new MenuExtendInfo();
        extendInfo.setOrder(menuExtendInfo.getOrder());
        extendInfo.setTitle(menuExtendInfo.getTitle());
        extendInfo.setIcon(menuExtendInfo.getIcon());
        extendInfo.setMenuType(menuExtendInfo.getMenuType());
        extendInfo.setVisible(menuExtendInfo.getVisible());
        extendInfo.setMenuStatus(menuExtendInfo.getMenuStatus());
        domain.setMenuExtendInfo(extendInfo);
        return domain;
    }
}
