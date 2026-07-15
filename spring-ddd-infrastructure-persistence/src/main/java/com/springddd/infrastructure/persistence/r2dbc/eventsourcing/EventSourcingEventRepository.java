package com.springddd.infrastructure.persistence.r2dbc.eventsourcing;

import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingEventEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface EventSourcingEventRepository
        extends ReactiveCrudRepository<EventSourcingEventEntity, Long> {

    @Query("SELECT * FROM event_sourcing_event WHERE entity_id = :entityId AND delete_status = 0 ORDER BY event_time ASC, id ASC")
    Flux<EventSourcingEventEntity> findHistoryEvents(String entityId);

    @Query("SELECT * FROM event_sourcing_event WHERE entity_id = :entityId AND delete_status = 0 AND event_time > :eventTime ORDER BY event_time ASC, id ASC")
    Flux<EventSourcingEventEntity> findEventsAfter(String entityId, LocalDateTime eventTime);
}
