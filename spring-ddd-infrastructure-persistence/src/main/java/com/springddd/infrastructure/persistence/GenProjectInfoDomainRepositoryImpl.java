package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class GenProjectInfoDomainRepositoryImpl implements GenProjectInfoDomainRepository {

    private final GenProjectInfoRepository genProjectInfoRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<GenProjectInfoDomain> load(InfoId aggregateRootId) {
        return genProjectInfoRepository.findById(aggregateRootId.value())
                .map(entityFactory::createGenProjectInfoDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(GenProjectInfoDomain aggregateRoot) {
        GenProjectInfoEntity entity = entityFactory.createGenProjectInfoEntity(aggregateRoot);
        return genProjectInfoRepository.save(entity).map(GenProjectInfoEntity::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(GenProjectInfoDomain aggregateRoot) {
        return genProjectInfoRepository.deleteById(aggregateRoot.getInfoId().value());
    }
}
