package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreGenColumnBindDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
