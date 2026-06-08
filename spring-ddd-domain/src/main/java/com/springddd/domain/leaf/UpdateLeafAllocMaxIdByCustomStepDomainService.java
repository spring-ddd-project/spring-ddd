package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

public interface UpdateLeafAllocMaxIdByCustomStepDomainService {

    Mono<Void> updateMaxIdByCustomStep(LeafAllocDomain domain);
}
