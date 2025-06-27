package com.springddd.domain.dict;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeSysDictItemByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
