package com.springddd.application.service.eventsourcing;

import com.springddd.domain.AggregateRootId;
import com.springddd.domain.eventsourcing.DomainEvent;
import com.springddd.domain.eventsourcing.EventSourcingAggregateRoot;
import com.springddd.domain.eventsourcing.EventSourcingRepository;
import com.springddd.infrastructure.publisher.eventsourcing.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
@RequiredArgsConstructor
public abstract class AbstractEventSourcingCommandService<
        ID extends AggregateRootId<?>,
        E extends EventSourcingAggregateRoot> {

    private final EventSourcingRepository<ID, E> repository;
    private final DomainEventPublisher eventPublisher;
    protected Mono<Void> execute(ID aggregateRootId, Consumer<E> action) {
        return repository.load(aggregateRootId)
                .doOnNext(action)
                .flatMap(aggregate -> {
                    List<DomainEvent> events = new ArrayList<>(aggregate.getDomainEvents());
                    return repository.save(aggregate)
                            .then(publishEvents(events));
                });
    }
    protected <R> Mono<R> executeAndReturn(ID aggregateRootId, Function<E, Tuple2<R, E>> action) {
        return repository.load(aggregateRootId)
                .map(action)
                .flatMap(tuple -> {
                    List<DomainEvent> events = new ArrayList<>(tuple.getT2().getDomainEvents());
                    return repository.save(tuple.getT2())
                            .then(publishEvents(events))
                            .thenReturn(tuple.getT1());
                });
    }

    private Mono<Void> publishEvents(List<DomainEvent> events) {
        if (events.isEmpty()) {
            return Mono.empty();
        }
        return eventPublisher.publish(events);
    }
}
