package com.springddd.domain.menu;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RestoreSysMenuByIdDomainService {

    Mono<Void> restoreByIds(List<Long> ids);
}
