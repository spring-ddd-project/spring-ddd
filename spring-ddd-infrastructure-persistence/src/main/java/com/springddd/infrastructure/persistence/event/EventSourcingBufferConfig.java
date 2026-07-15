package com.springddd.infrastructure.persistence.event;

import com.springddd.infrastructure.persistence.event.buffer.EventSourcingEventBuffer;
import com.springddd.infrastructure.persistence.event.buffer.SaveEntryHandler;
import com.springddd.infrastructure.persistence.r2dbc.event.EventSourcingEventBulkRepository;
import com.springddd.infrastructure.persistence.r2dbc.event.EventSourcingSnapshotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class EventSourcingBufferConfig {

    @Bean
    public EventSourcingJson eventSourcingJson(ObjectMapper objectMapper) {
        return new EventSourcingJson(objectMapper);
    }

    @Bean
    public SaveEntryHandler saveEntryHandler(
            @Value("${eventsourcing.buffer.batch-size:100}") int batchSize,
            @Value("${eventsourcing.buffer.flush-interval:50ms}") Duration flushInterval,
            EventSourcingEventBulkRepository bulkRepository,
            EventSourcingSnapshotRepository snapshotRepository,
            EventSourcingJson eventSourcingJson) {
        return new SaveEntryHandler(batchSize, flushInterval.toNanos(), bulkRepository, snapshotRepository, eventSourcingJson);
    }

    @Bean(destroyMethod = "shutdown")
    public EventSourcingEventBuffer eventSourcingEventBuffer(
            @Value("${eventsourcing.buffer.ring-size:1024}") int ringSize,
            @Value("${eventsourcing.buffer.batch-size:100}") int batchSize,
            @Value("${eventsourcing.buffer.flush-interval:50ms}") Duration flushInterval,
            SaveEntryHandler handler) {
        return new EventSourcingEventBuffer(ringSize, batchSize, flushInterval, handler);
    }
}
