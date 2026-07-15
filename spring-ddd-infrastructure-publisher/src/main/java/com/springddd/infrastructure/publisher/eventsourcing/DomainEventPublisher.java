package com.springddd.infrastructure.publisher.eventsourcing;

import com.springddd.domain.eventsourcing.DomainEvent;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DomainEventPublisher {

    default Mono<Void> publish(DomainEvent event) {
        return publish(List.of(event));
    }

    Mono<Void> publish(List<DomainEvent> events);
}
