package com.springddd.domain;

import reactor.core.publisher.Mono;

public interface DomainRepository<T extends AggregateRootId<?>, E extends AbstractDomainMask> {

    Mono<E> load(T aggregateRootId);

    Mono<Void> save(E aggregateRoot);
}
