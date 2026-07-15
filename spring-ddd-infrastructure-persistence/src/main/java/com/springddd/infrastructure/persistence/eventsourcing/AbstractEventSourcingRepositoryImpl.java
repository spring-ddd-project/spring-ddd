package com.springddd.infrastructure.persistence.eventsourcing;

import com.springddd.domain.AggregateRootId;
import com.springddd.domain.eventsourcing.DomainEvent;
import com.springddd.domain.eventsourcing.EventSourcingAggregateRoot;
import com.springddd.domain.eventsourcing.EventSourcingException;
import com.springddd.domain.eventsourcing.EventSourcingRepository;
import com.springddd.domain.util.ErrorCode;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingEventEntity;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingSnapshotEntity;
import com.springddd.infrastructure.persistence.eventsourcing.buffer.EventSourcingEventBuffer;
import com.springddd.infrastructure.persistence.eventsourcing.cache.EventSourcingSnapshotCache;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingEventRepository;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public abstract class AbstractEventSourcingRepositoryImpl<
        ID extends AggregateRootId<?>,
        E extends EventSourcingAggregateRoot>
        implements EventSourcingRepository<ID, E> {

    private final EventSourcingEventBuffer eventBuffer;
    private final EventSourcingEventRepository eventRepository;
    private final EventSourcingSnapshotRepository snapshotRepository;
    private final EventSourcingSnapshotCache snapshotCache;
    private final EventSourcingJson eventSourcingJson;

    protected abstract String aggregateType();

    protected abstract E newAggregateRoot();

    protected abstract String entityId(ID aggregateRootId);

    @Override
    public Mono<Void> save(E aggregateRoot) {
        List<DomainEvent> domainEvents = aggregateRoot.getDomainEvents();
        if (domainEvents.isEmpty()) {
            return Mono.empty();
        }

        String entityId = extractEntityId(aggregateRoot);
        DomainEvent lastEvent = domainEvents.get(domainEvents.size() - 1);

        String snapshotJson = null;
        if (Boolean.TRUE.equals(aggregateRoot.getTakeSnapshot())) {
            aggregateRoot.getDomainEvents().clear();
            aggregateRoot.setTakeSnapshot(Boolean.FALSE);
            snapshotJson = eventSourcingJson.toJson(aggregateRoot);
        }

        return eventBuffer.submit(entityId, aggregateType(), domainEvents, snapshotJson,
                        lastEvent.getEventId(), lastEvent.getEventTime())
                .doFinally(signal -> aggregateRoot.clearDomainEvents());
    }

    @Override
    public Mono<E> load(ID aggregateRootId) {
        String entityId = entityId(aggregateRootId);

        return snapshotCache.findByEntityId(entityId)
                .switchIfEmpty(snapshotRepository.findByEntityId(entityId)
                        .doOnNext(snapshot -> snapshotCache.put(entityId, snapshot)))
                .flatMap(snapshot -> loadFromSnapshot(snapshot, entityId))
                .switchIfEmpty(Mono.defer(() -> loadFromScratch(entityId)));
    }

    private Mono<E> loadFromSnapshot(EventSourcingSnapshotEntity snapshot, String entityId) {
        E aggregateRoot = eventSourcingJson.toObject(snapshot.getEntityData(), aggregateClass());
        aggregateRoot.setTakeSnapshot(Boolean.FALSE);
        return replayEvents(aggregateRoot, () -> eventRepository.findEventsAfter(entityId, snapshot.getEventTime()));
    }

    private Mono<E> loadFromScratch(String entityId) {
        E aggregateRoot = newAggregateRoot();
        return replayEvents(aggregateRoot, () -> eventRepository.findHistoryEvents(entityId));
    }

    private Mono<E> replayEvents(E aggregateRoot, Supplier<Flux<EventSourcingEventEntity>> eventSupplier) {
        return eventSupplier.get()
                .map(eventEntity -> eventSourcingJson.toDomainEvent(eventEntity.getEventType(), eventEntity.getEventData()))
                .collectList()
                .doOnNext(aggregateRoot::rebuild)
                .thenReturn(aggregateRoot);
    }

    private String extractEntityId(E aggregateRoot) {
        String entityId = entityIdFromAggregateRoot(aggregateRoot);
        if (Objects.isNull(entityId)) {
            throw new EventSourcingException(ErrorCode.EVENT_SOURCING_ENTITY_ID_NULL);
        }
        return entityId;
    }

    protected abstract String entityIdFromAggregateRoot(E aggregateRoot);

    protected abstract Class<E> aggregateClass();
}
