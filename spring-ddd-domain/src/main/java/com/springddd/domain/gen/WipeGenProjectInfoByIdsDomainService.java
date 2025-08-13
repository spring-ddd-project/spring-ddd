package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeGenProjectInfoByIdsDomainService {

    Mono<Void> wipeByIds(List<Long> ids);
}
