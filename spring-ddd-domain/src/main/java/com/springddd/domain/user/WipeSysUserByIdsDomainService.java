package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeSysUserByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
