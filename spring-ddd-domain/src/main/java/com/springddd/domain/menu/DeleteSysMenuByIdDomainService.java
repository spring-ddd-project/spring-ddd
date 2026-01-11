package com.springddd.domain.menu;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteSysMenuByIdDomainService {

    Mono<Void> deleteByIds(List<Long> ids);
}
