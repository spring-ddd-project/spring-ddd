package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class GenColumnsDomainRepositoryImpl implements GenColumnsDomainRepository {

    private final GenColumnsRepository genColumnsRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<GenColumnsDomain> load(ColumnsId aggregateRootId) {
        return genColumnsRepository.findById(aggregateRootId.value())
                .map(entityFactory::createGenColumnsDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(GenColumnsDomain aggregateRoot) {
        GenColumnsEntity entity = entityFactory.createGenColumnsEntity(aggregateRoot);
        return genColumnsRepository.save(entity).map(GenColumnsEntity::getId);
    }
}
