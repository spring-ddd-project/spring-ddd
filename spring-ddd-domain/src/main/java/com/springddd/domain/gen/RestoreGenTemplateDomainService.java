package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreGenTemplateDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
