package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenAggregateDomainRepositoryImpl implements GenAggregateDomainRepository {

    private final GenAggregateRepository genAggregateRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<GenAggregateDomain> load(AggregateId aggregateRootId) {
        return genAggregateRepository.findById(aggregateRootId.value())
                .map(entityFactory::createGenAggregateDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(GenAggregateDomain aggregateRoot) {
        GenAggregateEntity entity = entityFactory.createGenAggregateEntity(aggregateRoot);
        return genAggregateRepository.save(entity).map(GenAggregateEntity::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(GenAggregateDomain aggregateRoot) {
        return genAggregateRepository.deleteById(aggregateRoot.getAggregateId().value());
    }
}
