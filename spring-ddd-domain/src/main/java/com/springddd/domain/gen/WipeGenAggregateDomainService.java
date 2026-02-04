package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeGenAggregateDomainService {

    Mono<Void> wipe(List<Long> ids);
}
