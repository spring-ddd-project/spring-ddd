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

            MenuBasicInfo menuBasicInfo = new MenuBasicInfo(e.getName(), e.getPath(), e.getComponent(), e.getApi(), e.getRedirect(), e.getPermission());
            sysMenuDomain.setMenuBasicInfo(menuBasicInfo);

            MenuExtendInfo menuExtendInfo = new MenuExtendInfo(e.getSortOrder(), e.getTitle(), e.getAffixTab(), e.getNoBasicLayout(), e.getIcon(), e.getMenuType(), e.getVisible(), e.getEmbedded(), e.getMenuStatus());
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
        entity.setName(menuBasicInfo.menuName());
        entity.setPermission(menuBasicInfo.menuPermission());
        entity.setRedirect(menuBasicInfo.menuRedirect());
        entity.setPath(menuBasicInfo.menuPath());
        entity.setComponent(menuBasicInfo.menuComponent());
        entity.setApi(menuBasicInfo.api());

        MenuExtendInfo menuExtendInfo = aggregateRoot.getMenuExtendInfo();
        entity.setSortOrder(menuExtendInfo.order());
        entity.setTitle(menuExtendInfo.title());
        entity.setAffixTab(menuExtendInfo.affixTab());
        entity.setNoBasicLayout(menuExtendInfo.noBasicLayout());
        entity.setIcon(menuExtendInfo.icon());
        entity.setMenuType(menuExtendInfo.menuType());
        entity.setVisible(menuExtendInfo.visible());
        entity.setEmbedded(menuExtendInfo.embedded());
        entity.setMenuStatus(menuExtendInfo.menuStatus());

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
