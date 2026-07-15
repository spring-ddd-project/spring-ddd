package com.springddd.infrastructure.persistence.eventsourcing.buffer;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.springddd.domain.eventsourcing.DomainEvent;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class EventSourcingEventBuffer {

    private final Disruptor<SaveEntry> disruptor;
    private final RingBuffer<SaveEntry> ringBuffer;

    public EventSourcingEventBuffer(int bufferSize,
                                     int batchSize,
                                     Duration flushInterval,
                                     SaveEntryHandler handler) {
        SaveEntryFactory factory = new SaveEntryFactory();
        this.disruptor = new Disruptor<>(
                factory,
                bufferSize,
                Executors.defaultThreadFactory(),
                ProducerType.MULTI,
                new com.lmax.disruptor.BlockingWaitStrategy());
        this.disruptor.handleEventsWith(handler);
        this.ringBuffer = this.disruptor.start();
    }

    public Mono<Void> submit(String entityId,
                               String aggregateType,
                               List<DomainEvent> events,
                               String snapshotJson,
                               String lastEventId,
                               java.time.LocalDateTime lastEventTime) {
        if (events == null || events.isEmpty()) {
            return Mono.empty();
        }
        CompletableFuture<Void> future = new CompletableFuture<>();
        long sequence = ringBuffer.next();
        try {
            SaveEntry entry = ringBuffer.get(sequence);
            entry.setEntityId(entityId);
            entry.setAggregateType(aggregateType);
            entry.setEvents(events);
            entry.setSnapshotJson(snapshotJson);
            entry.setLastEventId(lastEventId);
            entry.setLastEventTime(lastEventTime);
            entry.setFuture(future);
        } finally {
            ringBuffer.publish(sequence);
        }
        return Mono.fromFuture(future);
    }

    @PreDestroy
    public void shutdown() {
        disruptor.shutdown();
    }
}
