package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LeafAllocRepository extends ReactiveCrudRepository<LeafAllocEntity, Long> {

    Mono<LeafAllocEntity> findByBizTag(String bizTag);

    @Query("SELECT biz_tag FROM leaf_alloc")
    Flux<String> findAllBizTags();

    @Query("UPDATE leaf_alloc SET max_id = max_id + :step, step = :step WHERE biz_tag = :bizTag")
    Mono<Integer> updateMaxIdByCustomStep(String bizTag, int step);
}
