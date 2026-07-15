package com.springddd.infrastructure.publisher.event;

import com.springddd.domain.event.DomainEvent;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DomainEventPublisher {

    default Mono<Void> publish(DomainEvent event) {
        return publish(List.of(event));
    }

    Mono<Void> publish(List<DomainEvent> events);
}
