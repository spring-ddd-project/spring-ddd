package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreSysUserByIdDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
