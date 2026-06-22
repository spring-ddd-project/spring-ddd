package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeLeafWorkerDomainService {

    Mono<Void> wipeByIds(List<Long> ids);

}