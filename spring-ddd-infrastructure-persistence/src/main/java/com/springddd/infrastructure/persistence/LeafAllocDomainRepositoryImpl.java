package com.springddd.infrastructure.persistence;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafAllocId;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class LeafAllocDomainRepositoryImpl implements LeafAllocDomainRepository {

    private final LeafAllocRepository leafAllocRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<LeafAllocDomain> load(LeafAllocId aggregateRootId) {
        return leafAllocRepository.findByBizTag(aggregateRootId.value())
                .map(entityFactory::createLeafAllocDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(LeafAllocDomain aggregateRoot) {
        LeafAllocEntity entity = entityFactory.createLeafAllocEntity(aggregateRoot);
        return leafAllocRepository.save(entity).map(LeafAllocEntity::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(LeafAllocDomain aggregateRoot) {
        return leafAllocRepository.deleteById(aggregateRoot.getId());
    }
}
