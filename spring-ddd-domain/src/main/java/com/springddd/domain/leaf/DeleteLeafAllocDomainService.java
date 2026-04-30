package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteLeafAllocDomainService {

    Mono<Void> deleteByIds(List<Long> ids);

}