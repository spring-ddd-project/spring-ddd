package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface BatchRestoreSysUserByIdDomainService {

    Mono<Void> restore(List<Long> ids);
}
