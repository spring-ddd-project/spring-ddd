package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreLeafAllocDomainService {

    Mono<Void> restoreByIds(List<Long> ids);

}
