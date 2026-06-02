package com.springddd.domain.leaf.service;

import com.springddd.domain.leaf.LeafAllocId;
import reactor.core.publisher.Mono;

public interface WipeLeafAllocByIdDomainService {

    Mono<Void> wipe(LeafAllocId id);
}
