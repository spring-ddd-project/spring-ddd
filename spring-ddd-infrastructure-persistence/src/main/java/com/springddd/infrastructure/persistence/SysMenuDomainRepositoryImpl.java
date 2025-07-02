package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
            menuBasicInfo.setMenuRedirect(new MenuRedirect(e.getRedirect()));
            menuBasicInfo.setMenuPermission(new MenuPermission(e.getPermission()));
            menuBasicInfo.setMenuPath(new MenuPath(e.getPath()));
            menuBasicInfo.setMenuComponent(new MenuComponent(e.getComponent()));
            sysMenuDomain.setMenuBasicInfo(menuBasicInfo);

            MenuExtendInfo menuExtendInfo = new MenuExtendInfo();
            menuExtendInfo.setOrder(e.getSortOrder());
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<Long> save(SysMenuDomain aggregateRoot) {
        SysMenuEntity entity = new SysMenuEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getMenuId()).map(MenuId::value).orElse(null));
        entity.setParentId(Optional.ofNullable(aggregateRoot.getParentId()).map(MenuId::value).orElse(null));

        MenuBasicInfo menuBasicInfo = aggregateRoot.getMenuBasicInfo();
        entity.setName(menuBasicInfo.getMenuName().value());
        entity.setPermission(Optional.ofNullable(menuBasicInfo.getMenuPermission()).map(MenuPermission::value).orElse(null));
        entity.setRedirect(Optional.ofNullable(menuBasicInfo.getMenuRedirect()).map(MenuRedirect::value).orElse(null));
        entity.setPath(Optional.ofNullable(menuBasicInfo.getMenuPath()).map(MenuPath::value).orElse(null));
        entity.setComponent(Optional.ofNullable(menuBasicInfo.getMenuComponent()).map(MenuComponent::value).orElse(null));

        MenuExtendInfo menuExtendInfo = aggregateRoot.getMenuExtendInfo();
        entity.setSortOrder(menuExtendInfo.getOrder());
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
