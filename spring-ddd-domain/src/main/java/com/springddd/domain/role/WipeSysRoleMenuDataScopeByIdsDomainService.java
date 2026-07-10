package com.springddd.domain.role;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeSysRoleMenuDataScopeByIdsDomainService {

    Mono<Void> wipeByIds(List<Long> ids);
}
