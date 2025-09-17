package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

public interface GenerateDomainService {

    Mono<Void> generate(String tableName, String projectName);
}
