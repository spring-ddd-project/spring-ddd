package com.springddd.domain.leaf.service;

import reactor.core.publisher.Mono;

public interface LeafSegmentIdGenerateDomainService {

    Mono<Long> nextId(String bizTag);
}
