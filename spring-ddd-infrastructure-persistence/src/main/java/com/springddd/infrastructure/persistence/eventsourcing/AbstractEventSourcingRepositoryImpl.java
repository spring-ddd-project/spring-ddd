package com.springddd.infrastructure.persistence.eventsourcing;

import com.springddd.domain.AggregateRootId;
import com.springddd.domain.eventsourcing.DomainEvent;
import com.springddd.domain.eventsourcing.EventSourcingAggregateRoot;
import com.springddd.domain.eventsourcing.EventSourcingRepository;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingEventEntity;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingSnapshotEntity;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingEventRepository;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 事件溯源仓库抽象实现。
 *
 * <p>仅使用两张表：事件表（event_sourcing_event）和快照表（event_sourcing_snapshot）。
 *
 * @param <ID> 聚合根 ID 类型
 * @param <E>  聚合根类型
 */
@Repository
@RequiredArgsConstructor
public abstract class AbstractEventSourcingRepositoryImpl<
        ID extends AggregateRootId<?>,
        E extends EventSourcingAggregateRoot>
        implements EventSourcingRepository<ID, E> {

    private final EventSourcingEventRepository eventRepository;
    private final EventSourcingSnapshotRepository snapshotRepository;
    private final EventSourcingJson eventSourcingJson;

    /**
     * 聚合根类型名称，用于区分不同业务的快照。
     */
    protected abstract String aggregateType();

    /**
     * 创建一个新的空聚合根实例，用于事件回放。
     */
    protected abstract E newAggregateRoot();

    /**
     * 从聚合根 ID 中提取字符串形式的实体 ID。
     */
    protected abstract String entityId(ID aggregateRootId);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> save(E aggregateRoot) {
        List<DomainEvent> domainEvents = aggregateRoot.getDomainEvents();
        if (domainEvents.isEmpty()) {
            return Mono.empty();
        }

        String entityId = extractEntityId(aggregateRoot);

        return saveEvents(domainEvents, entityId)
                .then(Mono.defer(() -> {
                    if (Boolean.TRUE.equals(aggregateRoot.getTakeSnapshot())) {
                        return saveSnapshot(aggregateRoot, domainEvents, entityId);
                    }
                    return Mono.empty();
                }))
                .doFinally(signal -> aggregateRoot.clearDomainEvents());
    }

    @Override
    public Mono<E> load(ID aggregateRootId) {
        String entityId = entityId(aggregateRootId);

        return snapshotRepository.findByEntityId(entityId)
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

    private Mono<Void> saveEvents(List<DomainEvent> domainEvents, String entityId) {
        return Flux.fromIterable(domainEvents)
                .map(event -> {
                    EventSourcingEventEntity entity = new EventSourcingEventEntity();
                    entity.setEventId(event.getEventId());
                    entity.setEntityId(entityId);
                    entity.setEventType(event.getEventType());
                    entity.setEventData(eventSourcingJson.toJson(event));
                    entity.setEventTime(event.getEventTime());
                    entity.setDeleteStatus(false);
                    return entity;
                })
                .flatMap(eventRepository::save)
                .then();
    }

    private Mono<Void> saveSnapshot(E aggregateRoot, List<DomainEvent> domainEvents, String entityId) {
        DomainEvent lastEvent = domainEvents.get(domainEvents.size() - 1);

        aggregateRoot.getDomainEvents().clear();
        aggregateRoot.setTakeSnapshot(Boolean.FALSE);

        return snapshotRepository.findByEntityId(entityId)
                .defaultIfEmpty(new EventSourcingSnapshotEntity())
                .flatMap(snapshot -> {
                    snapshot.setEntityId(entityId);
                    snapshot.setAggregateType(aggregateType());
                    snapshot.setEventId(lastEvent.getEventId());
                    snapshot.setEventTime(lastEvent.getEventTime());
                    snapshot.setEntityData(eventSourcingJson.toJson(aggregateRoot));
                    snapshot.setDeleteStatus(false);
                    return snapshotRepository.save(snapshot);
                })
                .then();
    }

    private String extractEntityId(E aggregateRoot) {
        String entityId = entityIdFromAggregateRoot(aggregateRoot);
        if (Objects.isNull(entityId)) {
            throw new IllegalStateException("聚合根 entityId 不能为空");
        }
        return entityId;
    }

    /**
     * 子类实现此方法，从聚合根中提取实体 ID。
     */
    protected abstract String entityIdFromAggregateRoot(E aggregateRoot);

    /**
     * 子类返回聚合根 Class，用于快照反序列化。
     */
    protected abstract Class<E> aggregateClass();
}
