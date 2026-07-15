package com.springddd.infrastructure.publisher.eventsourcing;

import com.springddd.domain.eventsourcing.DomainEvent;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 领域事件发布器接口。
 */
public interface DomainEventPublisher {

    /**
     * 发布单个领域事件。
     */
    default Mono<Void> publish(DomainEvent event) {
        return publish(List.of(event));
    }

    /**
     * 发布多个领域事件。
     */
    Mono<Void> publish(List<DomainEvent> events);
}
