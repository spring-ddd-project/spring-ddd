package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysUserPostRepository extends ReactiveCrudRepository<SysUserPostEntity, Long> {

    Flux<SysUserPostEntity> findByUserIdAndDeleteStatusFalse(Long userId);

    Mono<Void> deleteByUserId(Long userId);
}
