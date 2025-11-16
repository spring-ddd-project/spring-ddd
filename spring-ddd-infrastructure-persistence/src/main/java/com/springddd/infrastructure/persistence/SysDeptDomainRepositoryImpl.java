package com.springddd.infrastructure.persistence;

import com.springddd.domain.dept.*;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SysDeptDomainRepositoryImpl implements SysDeptDomainRepository {

    private final SysDeptRepository sysDeptRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<SysDeptDomain> load(DeptId aggregateRootId) {
        return sysDeptRepository.findById(aggregateRootId.value())
                .map(entityFactory::createSysDeptDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysDeptDomain aggregateRoot) {
        SysDeptEntity entity = entityFactory.createSysDeptEntity(aggregateRoot);
        return sysDeptRepository.save(entity).map(SysDeptEntity::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(SysDeptDomain aggregateRoot) {
        return sysDeptRepository.deleteById(aggregateRoot.getDeptId().value());
    }
}
