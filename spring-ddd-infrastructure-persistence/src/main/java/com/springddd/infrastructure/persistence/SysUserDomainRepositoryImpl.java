package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SysUserDomainRepositoryImpl implements SysUserDomainRepository {

    private final SysUserRepository sysUserRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<SysUserDomain> load(UserId aggregateRootId) {
        return sysUserRepository.findById(aggregateRootId.value())
                .map(entityFactory::createSysUserDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysUserDomain aggregateRoot) {
        SysUserEntity entity = entityFactory.createSysUserEntity(aggregateRoot);
        return sysUserRepository.save(entity).map(SysUserEntity::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(SysUserDomain aggregateRoot) {
        return sysUserRepository.deleteById(aggregateRoot.getUserId().value());
    }
}
