package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.mapper.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SysRoleDomainRepositoryImpl implements SysRoleDomainRepository {

    private final SysRoleRepository sysRoleRepository;

    @Override
    public Mono<SysRoleDomain> load(RoleId aggregateRootId) {
        return sysRoleRepository.findById(aggregateRootId.value()).flatMap(e -> {
            SysRoleDomain sysRoleDomain = new SysRoleDomain();
            sysRoleDomain.setRoleId(new RoleId(e.getId()));

            RoleBasicInfo roleBasicInfo = new RoleBasicInfo();
            roleBasicInfo.setRoleName(new RoleName(e.getRoleName()));
            roleBasicInfo.setRoleCode(new RoleCode(e.getRoleCode()));
            roleBasicInfo.setRoleDataScope(new RoleDataScope(e.getDataScope()));
            sysRoleDomain.setRoleBasicInfo(roleBasicInfo);

            RoleExtendInfo roleExtendInfo = new RoleExtendInfo();
            roleExtendInfo.setRoleDesc(e.getRoleDesc());
            roleExtendInfo.setRoleStatus(e.getRoleStatus());
            sysRoleDomain.setRoleExtendInfo(roleExtendInfo);

            sysRoleDomain.setDeptId(e.getDeptId());
            sysRoleDomain.setDeleteStatus(e.getDeleteStatus());
            sysRoleDomain.setVersion(e.getVersion());
            sysRoleDomain.setCreateBy(e.getCreateBy());
            sysRoleDomain.setCreateTime(e.getCreateTime());
            sysRoleDomain.setUpdateBy(e.getUpdateBy());
            sysRoleDomain.setUpdateTime(e.getUpdateTime());

            return Mono.just(sysRoleDomain);
        });
    }

    @Override
    public Mono<Long> save(SysRoleDomain aggregateRoot) {
        SysRoleEntity sysRoleEntity = new SysRoleEntity();

        sysRoleEntity.setId(Optional.ofNullable(aggregateRoot.getRoleId()).map(RoleId::value).orElse(null));

        RoleBasicInfo roleBasicInfo = aggregateRoot.getRoleBasicInfo();
        sysRoleEntity.setRoleName(roleBasicInfo.getRoleName().value());
        sysRoleEntity.setRoleCode(roleBasicInfo.getRoleCode().value());
        sysRoleEntity.setDataScope(roleBasicInfo.getRoleDataScope().value());

        RoleExtendInfo roleExtendInfo = aggregateRoot.getRoleExtendInfo();
        sysRoleEntity.setRoleDesc(roleExtendInfo.getRoleDesc());
        sysRoleEntity.setRoleStatus(roleExtendInfo.getRoleStatus());

        sysRoleEntity.setDeptId(aggregateRoot.getDeptId());
        sysRoleEntity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        sysRoleEntity.setVersion(aggregateRoot.getVersion());
        sysRoleEntity.setCreateBy(aggregateRoot.getCreateBy());
        sysRoleEntity.setCreateTime(aggregateRoot.getCreateTime());
        sysRoleEntity.setUpdateBy(aggregateRoot.getUpdateBy());
        sysRoleEntity.setUpdateTime(aggregateRoot.getUpdateTime());

        return sysRoleRepository.save(sysRoleEntity).map(SysRoleEntity::getId);
    }
}
