package com.springddd.infrastructure.persistence.eventsourcing.buffer;

import com.springddd.domain.eventsourcing.DomainEvent;
import com.springddd.infrastructure.persistence.eventsourcing.EventSourcingJson;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingEventBulkRepository;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingSnapshotRepository;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingEventEntity;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingSnapshotEntity;
import com.lmax.disruptor.EventHandler;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class SaveEntryHandler implements EventHandler<SaveEntry> {

    private final int batchSize;
    private final long flushIntervalNanos;
    private final EventSourcingEventBulkRepository bulkRepository;
    private final EventSourcingSnapshotRepository snapshotRepository;
    private final EventSourcingJson eventSourcingJson;

    private final List<SaveEntry> buffer = new ArrayList<>();
    private long lastFlushNanos = System.nanoTime();

    @Override
    public void onEvent(SaveEntry entry, long sequence, boolean endOfBatch) {
        buffer.add(entry);
        long now = System.nanoTime();
        boolean timeout = now - lastFlushNanos >= flushIntervalNanos;
        if (buffer.size() >= batchSize || timeout || endOfBatch) {
            flush();
            lastFlushNanos = System.nanoTime();
        }
    }

    private void flush() {
        if (buffer.isEmpty()) {
            return;
        }
        List<SaveEntry> batch = new ArrayList<>(buffer);
        buffer.clear();
        try {
            saveEvents(batch).block();
            saveSnapshots(batch).block();
            complete(batch, null);
        } catch (Exception ex) {
            complete(batch, ex);
        }
    }

    private Mono<Void> saveEvents(List<SaveEntry> batch) {
        List<EventSourcingEventEntity> entities = new ArrayList<>();
        for (SaveEntry entry : batch) {
            for (DomainEvent event : entry.getEvents()) {
                EventSourcingEventEntity entity = new EventSourcingEventEntity();
                entity.setEventId(event.getEventId());
                entity.setEntityId(entry.getEntityId());
                entity.setEventType(event.getEventType());
                entity.setEventData(eventSourcingJson.toJson(event));
                entity.setEventTime(event.getEventTime());
                entity.setDeleteStatus(false);
                entity.setVersion(0);
                entities.add(entity);
            }
        }
        if (entities.isEmpty()) {
            return Mono.empty();
        }
        return bulkRepository.saveAll(entities);
    }

    private Mono<Void> saveSnapshots(List<SaveEntry> batch) {
        List<Mono<Void>> ops = new ArrayList<>();
        for (SaveEntry entry : batch) {
            if (Objects.isNull(entry.getSnapshotJson())) {
                continue;
            }
            Mono<Void> op = snapshotRepository.findByEntityId(entry.getEntityId())
                    .defaultIfEmpty(new EventSourcingSnapshotEntity())
                    .flatMap(snapshot -> {
                        snapshot.setEntityId(entry.getEntityId());
                        snapshot.setAggregateType(entry.getAggregateType());
                        snapshot.setEntityData(entry.getSnapshotJson());
                        snapshot.setEventId(entry.getLastEventId());
                        snapshot.setEventTime(entry.getLastEventTime());
                        snapshot.setDeleteStatus(false);
                        return snapshotRepository.save(snapshot);
                    })
                    .then();
            ops.add(op);
        }
        if (ops.isEmpty()) {
            return Mono.empty();
        }
        return Mono.when(ops);
    }

    private void complete(List<SaveEntry> batch, Throwable ex) {
        for (SaveEntry entry : batch) {
            if (Objects.isNull(ex)) {
                entry.getFuture().complete(null);
            } else {
                entry.getFuture().completeExceptionally(ex);
            }
        }
    }
}
