package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

public interface UpdateLeafAllocMaxIdByTagDomainService {

    Mono<Void> updateMaxIdByTag(String tag);
}
