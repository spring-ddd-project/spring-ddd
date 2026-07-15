package com.springddd.domain.event;

import com.springddd.domain.AggregateRootId;
import reactor.core.publisher.Mono;
public interface EventSourcingRepository<ID extends AggregateRootId<?>, E extends EventSourcingAggregateRoot> {
    Mono<E> load(ID aggregateRootId);
    Mono<Void> save(E aggregateRoot);
}
