package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

public interface GenDownloadDomainService {

    Mono<Void> download();
}
