package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysUserPostByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
