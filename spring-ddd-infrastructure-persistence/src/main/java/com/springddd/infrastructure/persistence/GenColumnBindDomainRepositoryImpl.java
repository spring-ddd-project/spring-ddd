package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class GenColumnBindDomainRepositoryImpl implements GenColumnBindDomainRepository {

    private final GenColumnBindRepository genColumnBindRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<GenColumnBindDomain> load(ColumnBindId aggregateRootId) {
        return genColumnBindRepository.findById(aggregateRootId.value())
                .map(entityFactory::createGenColumnBindDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(GenColumnBindDomain aggregateRoot) {
        GenColumnBindEntity entity = entityFactory.createGenColumnBindEntity(aggregateRoot);
        return genColumnBindRepository.save(entity).map(GenColumnBindEntity::getId);
    }
}
