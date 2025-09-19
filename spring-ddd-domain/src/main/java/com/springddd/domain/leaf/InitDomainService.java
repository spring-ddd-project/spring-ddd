package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

public interface InitDomainService {

    Mono<Boolean> init();
}
