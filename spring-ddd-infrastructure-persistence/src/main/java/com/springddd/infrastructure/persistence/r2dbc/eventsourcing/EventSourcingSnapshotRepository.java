package com.springddd.infrastructure.persistence.r2dbc.eventsourcing;

import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingSnapshotEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface EventSourcingSnapshotRepository
        extends ReactiveCrudRepository<EventSourcingSnapshotEntity, Long> {

    @Query("SELECT * FROM event_sourcing_snapshot WHERE entity_id = :entityId AND delete_status = 0 LIMIT 1")
    Mono<EventSourcingSnapshotEntity> findByEntityId(String entityId);
}
