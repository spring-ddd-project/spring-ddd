package com.springddd.infrastructure.persistence.eventsourcing.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingSnapshotEntity;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingSnapshotRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class EventSourcingSnapshotCache {

    private final LoadingCache<String, EventSourcingSnapshotEntity> cache;

    public EventSourcingSnapshotCache(EventSourcingSnapshotRepository repository) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .refreshAfterWrite(Duration.ofMinutes(1))
                .build(key -> repository.findByEntityId(key).block());
    }

    public Mono<EventSourcingSnapshotEntity> findByEntityId(String entityId) {
        EventSourcingSnapshotEntity value = cache.get(entityId);
        return Mono.justOrEmpty(value);
    }

    public void invalidate(String entityId) {
        cache.invalidate(entityId);
    }

    public void put(String entityId, EventSourcingSnapshotEntity snapshot) {
        if (snapshot != null) {
            cache.put(entityId, snapshot);
        }
    }
}
