package com.springddd.domain.dict;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreSysDictItemByIdDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
