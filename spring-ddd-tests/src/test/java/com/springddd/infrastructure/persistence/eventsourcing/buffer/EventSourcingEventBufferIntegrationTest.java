package com.springddd.infrastructure.persistence.eventsourcing.buffer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.eventsourcing.DomainEvent;
import com.springddd.domain.eventsourcing.EventTypeMapping;
import com.springddd.domain.eventsourcing.fixture.TestAggregateRoot;
import com.springddd.domain.eventsourcing.fixture.TestCreated;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingSnapshotEntity;
import com.springddd.infrastructure.persistence.eventsourcing.EventSourcingJson;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingEventBulkRepository;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingSnapshotRepository;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventSourcingEventBufferIntegrationTest {

    private ConnectionFactory connectionFactory;
    private DatabaseClient databaseClient;
    private EventSourcingEventBulkRepository bulkRepository;
    private EventSourcingSnapshotRepository snapshotRepository;
    private EventSourcingJson eventSourcingJson;
    private EventSourcingEventBuffer buffer;

    @BeforeEach
    void setUp() {
        EventTypeMapping.clear();
        EventTypeMapping.register("TestCreated", TestCreated.class);

        connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, "127.0.0.1")
                .option(PORT, 33306)
                .option(USER, "test_user")
                .option(PASSWORD, "QAZ123wsx456")
                .option(DATABASE, "test_ddd")
                .build());
        databaseClient = DatabaseClient.builder().connectionFactory(connectionFactory).build();
        bulkRepository = new EventSourcingEventBulkRepository(databaseClient);
        snapshotRepository = mock(EventSourcingSnapshotRepository.class);
        when(snapshotRepository.findByEntityId(anyString())).thenReturn(Mono.empty());
        when(snapshotRepository.save(any())).thenReturn(Mono.just(new EventSourcingSnapshotEntity()));

        eventSourcingJson = new EventSourcingJson(new ObjectMapper());
        SaveEntryHandler handler = new SaveEntryHandler(
                100, Duration.ofMillis(50).toNanos(), bulkRepository, snapshotRepository, eventSourcingJson);
        buffer = new EventSourcingEventBuffer(1024, 100, Duration.ofMillis(50), handler);

        databaseClient.sql("TRUNCATE TABLE sys_event").then().block();
    }

    @AfterEach
    void tearDown() {
        buffer.shutdown();
        EventTypeMapping.clear();
    }

    @Test
    void bulkInsertShouldPersistEvents() throws InterruptedException {
        int total = 500;
        CountDownLatch latch = new CountDownLatch(total);
        List<Throwable> errors = new ArrayList<>();

        long start = System.nanoTime();
        for (int i = 0; i < total; i++) {
            String entityId = UUID.randomUUID().toString();
            DomainEvent event = TestCreated.of(entityId, "name" + i);
            buffer.submit(entityId, "TestAggregate", List.of(event), null, event.getEventId(), event.getEventTime())
                    .doFinally(signal -> latch.countDown())
                    .subscribe(v -> {}, errors::add);
        }
        assertTrue(latch.await(30, TimeUnit.SECONDS));
        long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        Long count = databaseClient.sql("SELECT COUNT(*) FROM sys_event")
                .map((row, metadata) -> row.get(0, Long.class))
                .first()
                .block();
        assertEquals(total, count);
        assertTrue(errors.isEmpty(), errors::toString);
        System.out.println("Bulk insert " + total + " events in " + millis + " ms, QPS=" + (total * 1000L / Math.max(1, millis)));
    }

    @Test
    void singleInsertBaseline() {
        databaseClient.sql("TRUNCATE TABLE sys_event").then().block();
        int total = 500;
        long start = System.nanoTime();
        for (int i = 0; i < total; i++) {
            String entityId = UUID.randomUUID().toString();
            DomainEvent event = TestCreated.of(entityId, "name" + i);
            databaseClient.sql("INSERT INTO sys_event (event_id, entity_id, event_type, event_data, event_time, delete_status, version) VALUES (:event_id, :entity_id, :event_type, :event_data, :event_time, 0, 0)")
                    .bind("event_id", event.getEventId())
                    .bind("entity_id", entityId)
                    .bind("event_type", event.getEventType())
                    .bind("event_data", eventSourcingJson.toJson(event))
                    .bind("event_time", event.getEventTime())
                    .then()
                    .block();
        }
        long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Long count = databaseClient.sql("SELECT COUNT(*) FROM sys_event")
                .map((row, metadata) -> row.get(0, Long.class))
                .first()
                .block();
        assertEquals(total, count);
        System.out.println("Single insert " + total + " events in " + millis + " ms, QPS=" + (total * 1000L / Math.max(1, millis)));
    }
}
