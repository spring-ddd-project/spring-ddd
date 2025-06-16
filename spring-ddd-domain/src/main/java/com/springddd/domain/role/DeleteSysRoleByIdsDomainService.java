package com.springddd.domain.role;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysRoleByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
