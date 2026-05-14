package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface LeafSegmentDomainService {

    Mono<Long> getId(String bizTag);

    Mono<Void> init();

    Mono<Boolean> isInitialized();

    Mono<Map<String, Object>> getCacheStatus();
}
