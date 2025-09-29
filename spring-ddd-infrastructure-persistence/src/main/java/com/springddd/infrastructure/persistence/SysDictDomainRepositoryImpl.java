package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SysDictDomainRepositoryImpl implements SysDictDomainRepository {

    private final SysDictRepository sysDictRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<SysDictDomain> load(DictId aggregateRootId) {
        return sysDictRepository.findById(aggregateRootId.value())
                .map(entityFactory::createSysDictDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysDictDomain aggregateRoot) {
        SysDictEntity entity = entityFactory.createSysDictEntity(aggregateRoot);
        return sysDictRepository.save(entity).map(SysDictEntity::getId);
    }
}
