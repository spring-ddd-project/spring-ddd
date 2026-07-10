package com.springddd.domain.post;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysPostByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
