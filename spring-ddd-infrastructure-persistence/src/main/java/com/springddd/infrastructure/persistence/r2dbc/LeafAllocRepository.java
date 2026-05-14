package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LeafAllocRepository extends ReactiveCrudRepository<LeafAllocEntity, Long> {

    @Modifying
    @Query("UPDATE leaf_alloc SET max_id = max_id + step, update_time = NOW(), version = version + 1 WHERE biz_tag = :bizTag")
    Mono<Integer> updateMaxId(String bizTag);

    @Modifying
    @Query("UPDATE leaf_alloc SET max_id = max_id + :step, update_time = NOW(), version = version + 1 WHERE biz_tag = :bizTag")
    Mono<Integer> updateMaxIdByCustomStep(String bizTag, int step);

    Mono<LeafAllocEntity> findByBizTag(String bizTag);

    @Query("SELECT biz_tag FROM leaf_alloc WHERE delete_status = false")
    Flux<String> findAllBizTags();
}
