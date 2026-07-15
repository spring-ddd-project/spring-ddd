package com.springddd.infrastructure.publisher.event;

import com.springddd.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Mono<Void> publish(List<DomainEvent> events) {
        return Mono.fromRunnable(() -> {
            for (DomainEvent event : events) {
                applicationEventPublisher.publishEvent(event);
            }
        });
    }
}
