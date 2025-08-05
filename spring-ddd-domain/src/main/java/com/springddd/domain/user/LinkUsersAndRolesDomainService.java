package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface LinkUsersAndRolesDomainService {

    Mono<Void> link(Long userId, List<Long> roleIds);
}
