package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysUserByIdDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
