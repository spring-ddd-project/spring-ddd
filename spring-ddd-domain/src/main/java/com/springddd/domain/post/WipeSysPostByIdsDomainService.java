package com.springddd.domain.post;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeSysPostByIdsDomainService {

    Mono<Void> wipeByIds(List<Long> ids);
}
