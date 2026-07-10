package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeSysUserPostByIdsDomainService {

    Mono<Void> wipeByIds(List<Long> ids);
}
