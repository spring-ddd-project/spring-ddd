package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteGenTemplateDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
