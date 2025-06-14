package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysUserByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
