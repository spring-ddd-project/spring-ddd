package com.springddd.infrastructure.persistence.r2dbc.event;

import com.springddd.infrastructure.persistence.entity.event.EventSourcingSnapshotEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface EventSourcingSnapshotRepository
        extends ReactiveCrudRepository<EventSourcingSnapshotEntity, Long> {

    @Query("SELECT * FROM sys_event_snapshot WHERE entity_id = :entityId AND delete_status = 0 LIMIT 1")
    Mono<EventSourcingSnapshotEntity> findByEntityId(String entityId);
}
