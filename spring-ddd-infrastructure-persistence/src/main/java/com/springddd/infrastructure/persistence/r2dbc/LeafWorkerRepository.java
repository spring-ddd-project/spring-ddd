package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.LeafWorkerEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LeafWorkerRepository extends ReactiveCrudRepository<LeafWorkerEntity, Long> {

    Mono<LeafWorkerEntity> findByIpAndPortAndDeleteStatusFalse(String ip, int port);

    Flux<LeafWorkerEntity> findByDeleteStatusFalseOrderByWorkerIdAsc();

    @Modifying
    @Query("UPDATE leaf_worker SET last_timestamp = :lastTimestamp, update_time = NOW() WHERE id = :id AND delete_status = false")
    Mono<Integer> updateLastTimestamp(Long id, Long lastTimestamp);

    @Modifying
    @Query("UPDATE leaf_worker SET delete_status = true WHERE last_timestamp < :expiredTimestamp AND delete_status = false")
    Mono<Integer> markExpiredWorkers(Long expiredTimestamp);
}
