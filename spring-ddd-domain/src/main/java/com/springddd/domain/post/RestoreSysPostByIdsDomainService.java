package com.springddd.domain.post;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreSysPostByIdsDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
