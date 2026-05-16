package com.springddd.domain.permission;

import reactor.core.publisher.Mono;

public interface ColumnPermissionEvaluator {

    <T> Mono<T> filter(T obj, String entityCode, MaskStrategy strategy);

    Mono<java.util.Set<String>> getVisibleColumns(String entityCode);
}
