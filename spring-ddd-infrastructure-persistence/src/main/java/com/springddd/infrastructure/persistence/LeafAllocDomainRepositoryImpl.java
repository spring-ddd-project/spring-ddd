package com.springddd.infrastructure.persistence;

import com.springddd.domain.leaf.*;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LeafAllocDomainRepositoryImpl implements LeafAllocDomainRepository {

    private final LeafAllocRepository repository;

    @Override
    public Mono<LeafAllocDomain> load(LeafId leafId) {
        return repository.findById(leafId.value()).map(e -> {
            LeafAllocDomain domain = new LeafAllocDomain();

            domain.setLeafId(new LeafId(e.getId()));

            domain.setLeafProp(new LeafProp(e.getBizTag(), e.getStep(), e.getMaxId()));
            domain.setExtendInfo(new ExtendInfo(e.getDescription()));

            domain.setUpdateTime(e.getUpdateTime());
            domain.setVersion(e.getVersion());

            return domain;
        });
    }

    @Override
    public Mono<Long> save(LeafAllocDomain domain) {
        LeafAllocEntity entity = new LeafAllocEntity();

        entity.setId(Optional.ofNullable(domain.getLeafId()).map(LeafId::value).orElse(null));

        entity.setBizTag(domain.getLeafProp().bizTag());
        entity.setStep(domain.getLeafProp().step());
        entity.setMaxId(domain.getLeafProp().maxId());
        entity.setDescription(domain.getExtendInfo().description());

        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());

        return repository.save(entity).map(LeafAllocEntity::getId);
    }
}
