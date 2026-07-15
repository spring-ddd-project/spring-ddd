package com.springddd.domain.eventsourcing.fixture;

import com.springddd.domain.eventsourcing.DomainEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestCreated extends DomainEvent {

    private String name;

    public static TestCreated of(String entityId, String name) {
        TestCreated event = new TestCreated();
        event.setEventId(entityId + "-created");
        event.setEntityId(entityId);
        event.setEventTime(LocalDateTime.now());
        event.setName(name);
        return event;
    }
}
