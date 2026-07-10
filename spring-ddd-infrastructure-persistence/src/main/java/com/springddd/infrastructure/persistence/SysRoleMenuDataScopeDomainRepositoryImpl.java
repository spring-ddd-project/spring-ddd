package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuDataScopeEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuDataScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysRoleMenuDataScopeDomainRepositoryImpl implements SysRoleMenuDataScopeDomainRepository {

    private final SysRoleMenuDataScopeRepository sysRoleMenuDataScopeRepository;

    @Override
    public Mono<SysRoleMenuDataScopeDomain> load(RoleMenuDataScopeId aggregateRootId) {
        return sysRoleMenuDataScopeRepository.findById(aggregateRootId.value()).map(entity -> {
            SysRoleMenuDataScopeDomain domain = new SysRoleMenuDataScopeDomain();

            domain.setId(new RoleMenuDataScopeId(entity.getId()));

            RoleMenuDataScopeInfo info = new RoleMenuDataScopeInfo(entity.getRoleId(), entity.getMenuId(), entity.getDataScope());
            domain.setRoleMenuDataScopeInfo(info);

            domain.setDeleteStatus(entity.getDeleteStatus());
            domain.setCreateBy(entity.getCreateBy());
            domain.setCreateTime(entity.getCreateTime());
            domain.setUpdateBy(entity.getUpdateBy());
            domain.setUpdateTime(entity.getUpdateTime());
            domain.setVersion(entity.getVersion());
            return domain;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysRoleMenuDataScopeDomain aggregateRoot) {
        SysRoleMenuDataScopeEntity entity = new SysRoleMenuDataScopeEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(RoleMenuDataScopeId::value).orElse(null));

        RoleMenuDataScopeInfo info = aggregateRoot.getRoleMenuDataScopeInfo();
        entity.setRoleId(info.roleId());
        entity.setMenuId(info.menuId());
        entity.setDataScope(info.dataScope());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return sysRoleMenuDataScopeRepository.save(entity).map(SysRoleMenuDataScopeEntity::getId);
    }
}
