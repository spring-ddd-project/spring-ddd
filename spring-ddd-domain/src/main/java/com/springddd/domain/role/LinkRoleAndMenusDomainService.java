package com.springddd.domain.role;

import reactor.core.publisher.Mono;

import java.util.List;

public interface LinkRoleAndMenusDomainService {

    Mono<Void> link(Long roleId, List<Long> menuIds);
}
