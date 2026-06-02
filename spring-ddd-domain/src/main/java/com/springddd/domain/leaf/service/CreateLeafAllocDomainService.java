package com.springddd.domain.leaf.service;

import com.springddd.domain.leaf.LeafAllocDomain;
import reactor.core.publisher.Mono;

public interface CreateLeafAllocDomainService {

    Mono<LeafAllocDomain> create(LeafAllocDomain domain);
}
