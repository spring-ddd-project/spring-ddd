package com.springddd.domain.event.fixture;

import com.springddd.domain.event.DomainEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestCountChanged extends DomainEvent {

    private Integer count;

    public static TestCountChanged of(String entityId, int count) {
        TestCountChanged event = new TestCountChanged();
        event.setEventId(entityId + "-count-" + count);
        event.setEntityId(entityId);
        event.setEventTime(LocalDateTime.now());
        event.setCount(count);
        return event;
    }
}
