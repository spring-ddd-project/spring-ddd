package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

public interface WipeGenDataDomainService {

    Mono<Void> wipe();
}
