package com.springddd.domain.dept;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreSysDeptByIdDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
