package com.springddd.infrastructure.persistence;

import com.springddd.domain.leaf.*;
import com.springddd.infrastructure.persistence.entity.LeafWorkerEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafWorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LeafWorkerDomainRepositoryImpl implements LeafWorkerDomainRepository {

    private final LeafWorkerRepository repository;

    @Override
    public Mono<LeafWorkerDomain> load(LeafId leafId) {
        return repository.findById(leafId.value()).map(e -> {
            LeafWorkerDomain domain = new LeafWorkerDomain();

            domain.setLeafId(new LeafId(e.getId()));

            domain.setWorker(new Worker(e.getWorkerId(), e.getDatacenterId()));
            domain.setAddress(new Address(e.getIp(), e.getPort()));
            domain.setExtendInfo(new ExtendInfo(e.getLastTimestamp(), e.getUpdateTime(), e.getDeleteStatus()));

            domain.setDeleteStatus(e.getDeleteStatus());
            domain.setUpdateTime(e.getUpdateTime());

            return domain;
        });
    }

    @Override
    public Mono<Long> save(LeafWorkerDomain domain) {
        LeafWorkerEntity entity = new LeafWorkerEntity();

        entity.setId(Optional.ofNullable(domain.getLeafId()).map(LeafId::value).orElse(null));

        entity.setWorkerId(domain.getWorker().workerId());
        entity.setDatacenterId(domain.getWorker().datacenterId());
        entity.setIp(domain.getAddress().ip());
        entity.setPort(domain.getAddress().port());
        entity.setLastTimestamp(domain.getExtendInfo().lastTimestamp());
        entity.setUpdateTime(domain.getExtendInfo().updateTime());
        entity.setDeleteStatus(domain.getExtendInfo().deleteStatus());

        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setUpdateTime(domain.getUpdateTime());

        return repository.save(entity).map(LeafWorkerEntity::getId);
    }
}
