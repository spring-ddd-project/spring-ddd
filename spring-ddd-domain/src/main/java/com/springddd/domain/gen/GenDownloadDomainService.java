package com.springddd.domain.gen;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface GenDownloadDomainService {

    Mono<ResponseEntity<Resource>> download();
}
