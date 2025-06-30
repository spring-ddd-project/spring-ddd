package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.SysUserRoleDomainRepository;
import com.springddd.domain.user.UserId;
import com.springddd.domain.user.UserRoleId;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysUserRoleDomainRepositoryImpl implements SysUserRoleDomainRepository {

    private final SysUserRoleRepository sysUserRoleRepository;

    @Override
    public Mono<SysUserRoleDomain> load(UserRoleId aggregateRootId) {
        return sysUserRoleRepository.findById(aggregateRootId.value()).flatMap(e -> {
            SysUserRoleDomain sysUserRoleDomain = new SysUserRoleDomain();

            sysUserRoleDomain.setUserRoleId(new UserRoleId(e.getId()));
            sysUserRoleDomain.setUserId(new UserId(e.getUserId()));
            sysUserRoleDomain.setRoleId(new RoleId(e.getRoleId()));

            sysUserRoleDomain.setDeptId(e.getDeptId());
            sysUserRoleDomain.setDeleteStatus(e.getDeleteStatus());
            sysUserRoleDomain.setVersion(e.getVersion());
            sysUserRoleDomain.setCreateBy(e.getCreateBy());
            sysUserRoleDomain.setCreateTime(e.getCreateTime());
            sysUserRoleDomain.setUpdateBy(e.getUpdateBy());
            sysUserRoleDomain.setUpdateTime(e.getUpdateTime());

            return Mono.just(sysUserRoleDomain);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysUserRoleDomain aggregateRoot) {
        SysUserRoleEntity entity = new SysUserRoleEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getUserRoleId()).map(UserRoleId::value).orElse(null));
        entity.setUserId(aggregateRoot.getUserId().value());
        entity.setRoleId(aggregateRoot.getRoleId().value());

        entity.setDeptId(aggregateRoot.getDeptId());
        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setVersion(aggregateRoot.getVersion());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());

        return sysUserRoleRepository.save(entity).map(SysUserRoleEntity::getId);
    }
}
