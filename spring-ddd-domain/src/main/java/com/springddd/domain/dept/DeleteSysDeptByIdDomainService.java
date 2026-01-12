package com.springddd.domain.dept;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysDeptByIdDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
