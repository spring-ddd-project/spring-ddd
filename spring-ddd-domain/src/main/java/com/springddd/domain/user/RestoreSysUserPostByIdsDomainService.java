package com.springddd.domain.user;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreSysUserPostByIdsDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
