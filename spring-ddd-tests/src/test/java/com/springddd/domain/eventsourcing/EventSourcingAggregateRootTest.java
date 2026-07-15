package com.springddd.domain.eventsourcing;

import com.springddd.domain.eventsourcing.fixture.TestAggregateRoot;
import com.springddd.domain.eventsourcing.fixture.TestCountChanged;
import com.springddd.domain.eventsourcing.fixture.TestCreated;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventSourcingAggregateRootTest {

    @Test
    void shouldRebuildStateFromEvents() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        List<DomainEvent> events = new ArrayList<>();
        events.add(TestCreated.of("1", "name1"));
        events.add(TestCountChanged.of("1", 5));

        aggregate.rebuild(events);

        assertEquals("1", aggregate.getEntityId());
        assertEquals("name1", aggregate.getName());
        assertEquals(5, aggregate.getCount());
    }

    @Test
    void shouldClearDomainEventsAfterRebuild() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        aggregate.rebuild(List.of(TestCreated.of("1", "name1")));

        assertTrue(aggregate.getDomainEvents().isEmpty());
    }

    @Test
    void shouldMarkTakeSnapshotWhenEventsExceedThreshold() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        List<DomainEvent> events = new ArrayList<>();
        for (int i = 0; i < EventSourcingAggregateRoot.DEFAULT_SNAPSHOT_THRESHOLD + 1; i++) {
            events.add(TestCountChanged.of("1", i));
        }

        aggregate.rebuild(events);

        assertTrue(aggregate.getTakeSnapshot());
    }

    @Test
    void shouldNotMarkTakeSnapshotWhenEventsBelowThreshold() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        aggregate.rebuild(List.of(TestCreated.of("1", "name1")));

        assertFalse(aggregate.getTakeSnapshot());
    }

    @Test
    void shouldApplyEventAndRegisterDomainEvent() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestCreated event = TestCreated.of("1", "name1");
        aggregate.apply(event);

        assertEquals("1", aggregate.getEntityId());
        assertEquals(1, aggregate.getDomainEvents().size());
    }

    @Test
    void shouldThrowExceptionWhenApplyMethodNotFound() {
        EventSourcingAggregateRoot aggregate = new EventSourcingAggregateRoot() {
        };
        DomainEvent event = TestCreated.of("1", "name1");

        assertThrows(EventSourcingException.class, () -> aggregate.apply(event));
    }
}
