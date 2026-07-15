package com.springddd.infrastructure.persistence.eventsourcing.buffer;

import com.springddd.domain.eventsourcing.DomainEvent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
public class SaveEntry {
    private String entityId;
    private String aggregateType;
    private List<DomainEvent> events;
    private String snapshotJson;
    private String lastEventId;
    private LocalDateTime lastEventTime;
    private CompletableFuture<Void> future;
}
