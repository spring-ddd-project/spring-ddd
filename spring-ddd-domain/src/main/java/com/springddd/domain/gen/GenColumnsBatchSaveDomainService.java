package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

import java.util.List;

public interface GenColumnsBatchSaveDomainService {

    Mono<Void> batchSave(List<GenColumnsDomain> domains);
}
