package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SysRoleDomainRepositoryImpl implements SysRoleDomainRepository {

    private final SysRoleRepository sysRoleRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<SysRoleDomain> load(RoleId aggregateRootId) {
        return sysRoleRepository.findById(aggregateRootId.value())
                .map(entityFactory::createSysRoleDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysRoleDomain aggregateRoot) {
        SysRoleEntity entity = entityFactory.createSysRoleEntity(aggregateRoot);
        return sysRoleRepository.save(entity).map(SysRoleEntity::getId);
    }
}
