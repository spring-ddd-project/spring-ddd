package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class SysRoleDomainRepositoryImpl implements SysRoleDomainRepository {

    private final SysRoleRepository sysRoleRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Mono<SysRoleDomain> load(RoleId aggregateRootId) {
        return sysRoleRepository.findById(aggregateRootId.value()).flatMap(e -> {
            SysRoleDomain sysRoleDomain = new SysRoleDomain();
            sysRoleDomain.setRoleId(new RoleId(e.getId()));

            RoleBasicInfo roleBasicInfo = new RoleBasicInfo(e.getRoleName(), e.getRoleCode(), e.getDataScope(), e.getOwnerStatus());
            sysRoleDomain.setRoleBasicInfo(roleBasicInfo);

            RoleExtendInfo roleExtendInfo = new RoleExtendInfo(e.getRoleDesc(), e.getRoleStatus());
            sysRoleDomain.setRoleExtendInfo(roleExtendInfo);

            sysRoleDomain.setDeptId(e.getDeptId());

            if (e.getDataPermission() != null) {
                try {
                    sysRoleDomain.setDataPermission(objectMapper.readValue(e.getDataPermission(), DataPermission.class));
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException("Failed to read data permission config", ex);
                }
            }

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
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysRoleDomain aggregateRoot) {
        SysRoleEntity sysRoleEntity = new SysRoleEntity();

        sysRoleEntity.setId(Optional.ofNullable(aggregateRoot.getRoleId()).map(RoleId::value).orElse(null));

        RoleBasicInfo roleBasicInfo = aggregateRoot.getRoleBasicInfo();
        sysRoleEntity.setRoleName(roleBasicInfo.roleName());
        sysRoleEntity.setRoleCode(roleBasicInfo.roleCode());
        sysRoleEntity.setDataScope(roleBasicInfo.roleDataScope());
        sysRoleEntity.setOwnerStatus(roleBasicInfo.roleOwner());

        RoleExtendInfo roleExtendInfo = aggregateRoot.getRoleExtendInfo();
        sysRoleEntity.setRoleDesc(roleExtendInfo.roleDesc());
        sysRoleEntity.setRoleStatus(roleExtendInfo.roleStatus());

        sysRoleEntity.setDeptId(aggregateRoot.getDeptId());

        if (aggregateRoot.getDataPermission() != null) {
            try {
                sysRoleEntity.setDataPermission(objectMapper.writeValueAsString(aggregateRoot.getDataPermission()));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Failed to write data permission config", ex);
            }
        }

        sysRoleEntity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        sysRoleEntity.setVersion(aggregateRoot.getVersion());
        sysRoleEntity.setCreateBy(aggregateRoot.getCreateBy());
        sysRoleEntity.setCreateTime(aggregateRoot.getCreateTime());
        sysRoleEntity.setUpdateBy(aggregateRoot.getUpdateBy());
        sysRoleEntity.setUpdateTime(aggregateRoot.getUpdateTime());

        return sysRoleRepository.save(sysRoleEntity).map(SysRoleEntity::getId);
    }
}
