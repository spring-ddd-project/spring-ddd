package com.springddd.domain.eventsourcing;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class DomainEvent {
    private String eventId;
    private String entityId;
    private String eventType;
    private LocalDateTime eventTime;
    protected DomainEvent() {
        this.eventType = eventType();
    }
    protected String eventType() {
        return getClass().getSimpleName();
    }
}
