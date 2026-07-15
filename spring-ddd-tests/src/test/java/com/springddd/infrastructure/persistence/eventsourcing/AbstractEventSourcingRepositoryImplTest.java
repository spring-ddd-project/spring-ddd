package com.springddd.infrastructure.persistence.eventsourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.eventsourcing.EventTypeMapping;
import com.springddd.domain.eventsourcing.fixture.TestAggregateRoot;
import com.springddd.domain.eventsourcing.fixture.TestCountChanged;
import com.springddd.domain.eventsourcing.fixture.TestCreated;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingEventEntity;
import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingSnapshotEntity;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingEventRepository;
import com.springddd.infrastructure.persistence.r2dbc.eventsourcing.EventSourcingSnapshotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AbstractEventSourcingRepositoryImplTest {

    private EventSourcingEventRepository eventRepository;
    private EventSourcingSnapshotRepository snapshotRepository;
    private EventSourcingJson eventSourcingJson;
    private TestEventSourcingRepository repository;

    @BeforeEach
    void setUp() {
        EventTypeMapping.clear();
        EventTypeMapping.register("TestCreated", TestCreated.class);
        EventTypeMapping.register("TestCountChanged", TestCountChanged.class);

        eventRepository = Mockito.mock(EventSourcingEventRepository.class);
        snapshotRepository = Mockito.mock(EventSourcingSnapshotRepository.class);
        eventSourcingJson = new EventSourcingJson(new ObjectMapper());
        repository = new TestEventSourcingRepository(
                eventRepository, snapshotRepository, eventSourcingJson);
    }

    @AfterEach
    void tearDown() {
        EventTypeMapping.clear();
    }

    @Test
    void saveShouldPersistEvents() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        aggregate.apply(TestCreated.of("1", "name1"));

        when(eventRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(snapshotRepository.findByEntityId(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(repository.save(aggregate))
                .verifyComplete();
    }

    @Test
    void loadShouldRebuildFromEvents() {
        EventSourcingEventEntity eventEntity = new EventSourcingEventEntity();
        eventEntity.setEventType("TestCreated");
        eventEntity.setEventData(eventSourcingJson.toJson(TestCreated.of("1", "name1")));
        eventEntity.setEventTime(LocalDateTime.now());

        when(snapshotRepository.findByEntityId("1")).thenReturn(Mono.empty());
        when(eventRepository.findHistoryEvents("1")).thenReturn(Flux.just(eventEntity));

        StepVerifier.create(repository.load(new TestAggregateRoot.TestId("1")))
                .expectNextMatches(agg -> "1".equals(agg.getEntityId()) && "name1".equals(agg.getName()))
                .verifyComplete();
    }

    @Test
    void loadShouldRebuildFromSnapshotAndEvents() {
        TestAggregateRoot snapshotRoot = new TestAggregateRoot();
        snapshotRoot.setEntityId("1");
        snapshotRoot.setName("name1");
        snapshotRoot.setCount(3);

        EventSourcingSnapshotEntity snapshotEntity = new EventSourcingSnapshotEntity();
        snapshotEntity.setEntityData(eventSourcingJson.toJson(snapshotRoot));
        snapshotEntity.setEventTime(LocalDateTime.now().minusMinutes(1));

        EventSourcingEventEntity eventEntity = new EventSourcingEventEntity();
        eventEntity.setEventType("TestCountChanged");
        eventEntity.setEventData(eventSourcingJson.toJson(TestCountChanged.of("1", 5)));
        eventEntity.setEventTime(LocalDateTime.now());

        when(snapshotRepository.findByEntityId("1")).thenReturn(Mono.just(snapshotEntity));
        when(eventRepository.findEventsAfter(anyString(), any())).thenReturn(Flux.just(eventEntity));

        StepVerifier.create(repository.load(new TestAggregateRoot.TestId("1")))
                .expectNextMatches(agg -> "name1".equals(agg.getName()) && Integer.valueOf(5).equals(agg.getCount()))
                .verifyComplete();
    }

    @Test
    void saveShouldGenerateSnapshotWhenThresholdExceeded() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        aggregate.apply(TestCreated.of("1", "name1"));
        for (int i = 0; i < 25; i++) {
            aggregate.changeCount(i);
        }
        aggregate.setTakeSnapshot(Boolean.TRUE);

        when(eventRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(snapshotRepository.findByEntityId("1")).thenReturn(Mono.empty());
        when(snapshotRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(repository.save(aggregate))
                .verifyComplete();
    }

    @Test
    void saveShouldDoNothingWhenNoDomainEvents() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        aggregate.setEntityId("1");

        StepVerifier.create(repository.save(aggregate))
                .verifyComplete();
    }

    static class TestEventSourcingRepository
            extends AbstractEventSourcingRepositoryImpl<TestAggregateRoot.TestId, TestAggregateRoot> {

        TestEventSourcingRepository(EventSourcingEventRepository eventRepository,
                                    EventSourcingSnapshotRepository snapshotRepository,
                                    EventSourcingJson eventSourcingJson) {
            super(eventRepository, snapshotRepository, eventSourcingJson);
        }

        @Override
        protected String aggregateType() {
            return "TestAggregate";
        }

        @Override
        protected TestAggregateRoot newAggregateRoot() {
            return new TestAggregateRoot();
        }

        @Override
        protected String entityId(TestAggregateRoot.TestId aggregateRootId) {
            return aggregateRootId.value();
        }

        @Override
        protected String entityIdFromAggregateRoot(TestAggregateRoot aggregateRoot) {
            return aggregateRoot.getEntityId();
        }

        @Override
        protected Class<TestAggregateRoot> aggregateClass() {
            return TestAggregateRoot.class;
        }
    }
}
