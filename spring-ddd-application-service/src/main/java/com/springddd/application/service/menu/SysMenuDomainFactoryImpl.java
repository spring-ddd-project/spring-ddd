package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SysMenuDomainFactoryImpl implements SysMenuDomainFactory {

    private final List<SysMenuDomainStrategy> strategies;

    @Override
    public SysMenuDomain create(MenuId parentId, Catalog catalog, MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo, Long deptId) {
        SysMenuDomain domain = new SysMenuDomain();

        for (SysMenuDomainStrategy strategy : strategies) {
            if (strategy.check(menuExtendInfo.menuType())) {
                domain = strategy.handle(menuBasicInfo, menuExtendInfo);
            }
        }

        domain.setParentId(parentId);
        domain.setCatalog(catalog);

        domain.setDeptId(deptId);
        domain.setDeleteStatus(false);
        return domain;
    }
}
