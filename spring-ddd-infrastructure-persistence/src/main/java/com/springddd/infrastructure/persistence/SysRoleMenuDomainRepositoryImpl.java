package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.exception.MenuIdNullException;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.RoleMenuId;
import com.springddd.domain.role.SysRoleMenuDomain;
import com.springddd.domain.role.SysRoleMenuDomainRepository;
import com.springddd.domain.role.exception.RoleIdNullException;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysRoleMenuDomainRepositoryImpl implements SysRoleMenuDomainRepository {

    private final SysRoleMenuRepository sysRoleMenuRepository;

    @Override
    public Mono<SysRoleMenuDomain> load(RoleMenuId aggregateRootId) {
        return sysRoleMenuRepository.findById(aggregateRootId.value()).flatMap(e -> {
            SysRoleMenuDomain sysRoleMenuDomain = new SysRoleMenuDomain();

            sysRoleMenuDomain.setRoleMenuId(new RoleMenuId(e.getId()));
            sysRoleMenuDomain.setRoleId(new RoleId(e.getRoleId()));
            sysRoleMenuDomain.setMenuId(new MenuId(e.getMenuId()));

            sysRoleMenuDomain.setDeptId(e.getDeptId());
            sysRoleMenuDomain.setDeleteStatus(e.getDeleteStatus());
            sysRoleMenuDomain.setVersion(e.getVersion());
            sysRoleMenuDomain.setCreateBy(e.getCreateBy());
            sysRoleMenuDomain.setCreateTime(e.getCreateTime());
            sysRoleMenuDomain.setUpdateBy(e.getUpdateBy());
            sysRoleMenuDomain.setUpdateTime(e.getUpdateTime());

            return Mono.just(sysRoleMenuDomain);
        });
    }

    @Override
    public Mono<Long> save(SysRoleMenuDomain aggregateRoot) {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getRoleMenuId()).map(RoleMenuId::value).orElse(null));
        entity.setRoleId(Optional.ofNullable(aggregateRoot.getRoleId()).map(RoleId::value).orElseThrow(RoleIdNullException::new));
        entity.setMenuId(Optional.ofNullable(aggregateRoot.getMenuId()).map(MenuId::value).orElseThrow(MenuIdNullException::new));

        entity.setDeptId(aggregateRoot.getDeptId());
        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setVersion(aggregateRoot.getVersion());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());

        return sysRoleMenuRepository.save(entity).map(SysRoleMenuEntity::getId);
    }
}
