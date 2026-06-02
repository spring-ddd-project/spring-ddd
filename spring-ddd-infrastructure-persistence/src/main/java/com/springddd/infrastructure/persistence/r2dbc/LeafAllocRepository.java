package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LeafAllocRepository extends ReactiveCrudRepository<LeafAllocEntity, Long> {

    Mono<LeafAllocEntity> findByBizTag(String bizTag);
}
