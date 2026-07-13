package com.springddd.domain.role;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreSysRoleMenuDataScopeByIdsDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
