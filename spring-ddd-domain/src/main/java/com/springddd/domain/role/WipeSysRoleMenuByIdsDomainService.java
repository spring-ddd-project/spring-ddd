package com.springddd.domain.role;

import reactor.core.publisher.Mono;

import java.util.List;

public interface WipeSysRoleMenuByIdsDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
