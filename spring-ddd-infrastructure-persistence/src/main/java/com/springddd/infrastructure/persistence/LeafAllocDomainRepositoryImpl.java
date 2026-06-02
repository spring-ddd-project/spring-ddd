package com.springddd.infrastructure.persistence;

import com.springddd.domain.leaf.*;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LeafAllocDomainRepositoryImpl implements LeafAllocDomainRepository {

    private final LeafAllocRepository leafAllocRepository;

    @Override
    public Mono<LeafAllocDomain> load(LeafAllocId aggregateRootId) {
        return leafAllocRepository.findById(aggregateRootId.value()).map(this::toDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(LeafAllocDomain aggregateRoot) {
        LeafAllocEntity entity = toEntity(aggregateRoot);
        return leafAllocRepository.save(entity).map(LeafAllocEntity::getId);
    }

    public Mono<LeafAllocDomain> loadByBizTag(String bizTag) {
        return leafAllocRepository.findByBizTag(bizTag).map(this::toDomain);
    }

    private LeafAllocDomain toDomain(LeafAllocEntity e) {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setLeafAllocId(new LeafAllocId(e.getId()));
        domain.setBizTag(new BizTag(e.getBizTag()));
        domain.setMaxId(new MaxId(e.getMaxId()));
        domain.setStep(new Step(e.getStep()));
        domain.setDescription(new Description(e.getDescription()));
        domain.setDeptId(e.getDeptId());
        domain.setDeleteStatus(e.getDeleteStatus());
        domain.setCreateBy(e.getCreateBy());
        domain.setCreateTime(e.getCreateTime());
        domain.setUpdateBy(e.getUpdateBy());
        domain.setUpdateTime(e.getUpdateTime());
        domain.setVersion(e.getVersion());
        return domain;
    }

    private LeafAllocEntity toEntity(LeafAllocDomain domain) {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(Optional.ofNullable(domain.getLeafAllocId()).map(LeafAllocId::value).orElse(null));
        entity.setBizTag(domain.getBizTag().value());
        entity.setMaxId(domain.getMaxId().value());
        entity.setStep(domain.getStep().value());
        entity.setDescription(Optional.ofNullable(domain.getDescription()).map(Description::value).orElse(null));
        entity.setDeptId(domain.getDeptId());
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }
}
