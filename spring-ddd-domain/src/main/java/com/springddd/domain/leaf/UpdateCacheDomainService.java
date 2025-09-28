package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

public interface UpdateCacheDomainService {

    Mono<Void> updateCache();
}
