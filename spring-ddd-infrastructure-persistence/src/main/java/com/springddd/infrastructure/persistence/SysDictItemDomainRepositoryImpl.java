package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SysDictItemDomainRepositoryImpl implements SysDictItemDomainRepository {

    private final SysDictItemRepository sysDictItemRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<SysDictItemDomain> load(DictItemId aggregateRootId) {
        return sysDictItemRepository.findById(aggregateRootId.value())
                .map(entityFactory::createSysDictItemDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysDictItemDomain aggregateRoot) {
        SysDictItemEntity entity = entityFactory.createSysDictItemEntity(aggregateRoot);
        return sysDictItemRepository.save(entity).map(SysDictItemEntity::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(SysDictItemDomain aggregateRoot) {
        return sysDictItemRepository.deleteById(aggregateRoot.getDictItemId().value());
    }
}
