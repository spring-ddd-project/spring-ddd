package com.springddd.domain.dept;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeSysDeptByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
