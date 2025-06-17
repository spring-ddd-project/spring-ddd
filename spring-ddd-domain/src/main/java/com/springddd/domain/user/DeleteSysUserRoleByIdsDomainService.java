package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysUserRoleByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
