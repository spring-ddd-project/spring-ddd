package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

public interface UpdateCacheAtEveryMinuteDomainService {

    Mono<Void> updateCacheSchedule();
}
