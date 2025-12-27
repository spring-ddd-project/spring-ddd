package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysMenuDomainRepositoryImpl implements SysMenuDomainRepository {

    private final SysMenuRepository sysMenuRepository;

    @Override
    public Mono<SysMenuDomain> load(MenuId aggregateRootId) {
        return sysMenuRepository.findById(aggregateRootId.value()).flatMap(e -> {
            SysMenuDomain sysMenuDomain = new SysMenuDomain();
            sysMenuDomain.setMenuId(new MenuId(e.getId()));
            sysMenuDomain.setParentId(new MenuId(e.getParentId()));

            MenuBasicInfo menuBasicInfo = new MenuBasicInfo();
            menuBasicInfo.setMenuName(new MenuName(e.getName()));
            menuBasicInfo.setMenuPermission(new MenuPermission(e.getPermission()));
            menuBasicInfo.setMenuPath(new MenuPath(e.getPath()));
            menuBasicInfo.setMenuComponent(new MenuComponent(e.getComponent()));
            sysMenuDomain.setMenuBasicInfo(menuBasicInfo);

            MenuExtendInfo menuExtendInfo = new MenuExtendInfo();
            menuExtendInfo.setOrder(e.getOrder());
            menuExtendInfo.setTitle(e.getTitle());
            menuExtendInfo.setAffixTab(e.getAffixTab());
            menuExtendInfo.setNoBasicLayout(e.getNoBasicLayout());
            menuExtendInfo.setIcon(e.getIcon());
            menuExtendInfo.setMenuType(e.getMenuType());
            menuExtendInfo.setVisible(e.getVisible());
            menuExtendInfo.setEmbedded(e.getEmbedded());
            menuExtendInfo.setMenuStatus(e.getMenuStatus());
            sysMenuDomain.setMenuExtendInfo(menuExtendInfo);

            sysMenuDomain.setDeptId(e.getDeptId());
            sysMenuDomain.setDeleteStatus(e.getDeleteStatus());
            sysMenuDomain.setVersion(e.getVersion());
            sysMenuDomain.setCreateBy(e.getCreateBy());
            sysMenuDomain.setCreateTime(e.getCreateTime());
            sysMenuDomain.setUpdateBy(e.getUpdateBy());
            sysMenuDomain.setUpdateTime(e.getUpdateTime());
            return Mono.just(sysMenuDomain);
        });
    }

    @Override
    public Mono<Long> save(SysMenuDomain aggregateRoot) {
        SysMenuEntity entity = new SysMenuEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getMenuId()).map(MenuId::value).orElse(null));
        entity.setParentId(Optional.ofNullable(aggregateRoot.getParentId()).map(MenuId::value).orElse(null));

        MenuBasicInfo menuBasicInfo = aggregateRoot.getMenuBasicInfo();
        entity.setName(menuBasicInfo.getMenuName().value());
        entity.setPermission(menuBasicInfo.getMenuPermission().value());
        entity.setRedirect(menuBasicInfo.getMenuRedirect().value());
        entity.setPath(menuBasicInfo.getMenuPath().value());
        entity.setComponent(menuBasicInfo.getMenuComponent().value());

        MenuExtendInfo menuExtendInfo = aggregateRoot.getMenuExtendInfo();
        entity.setOrder(menuExtendInfo.getOrder());
        entity.setTitle(menuExtendInfo.getTitle());
        entity.setAffixTab(menuExtendInfo.getAffixTab());
        entity.setNoBasicLayout(menuExtendInfo.getNoBasicLayout());
        entity.setIcon(menuExtendInfo.getIcon());
        entity.setMenuType(menuExtendInfo.getMenuType());
        entity.setVisible(menuExtendInfo.getVisible());
        entity.setEmbedded(menuExtendInfo.getEmbedded());
        entity.setMenuStatus(menuExtendInfo.getMenuStatus());

        entity.setDeptId(aggregateRoot.getDeptId());
        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setVersion(aggregateRoot.getVersion());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());

        return sysMenuRepository.save(entity).map(SysMenuEntity::getId);
    }
}
