package com.springddd.application.service.permission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.permission.ColumnPermissionEvaluator;
import com.springddd.domain.permission.MaskStrategy;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnPermissionEvaluatorImpl implements ColumnPermissionEvaluator {

    private final ObjectMapper objectMapper;

    @Override
    public <T> Mono<T> filter(T obj, String entityCode, MaskStrategy strategy) {
        if (obj == null) {
            return Mono.empty();
        }
        return ReactiveSecurityUtils.getVisibleColumns(entityCode)
                .flatMap(visibleColumns -> {
                    if (visibleColumns.isEmpty()) {
                        return Mono.just(obj);
                    }
                    return applyFilter(obj, visibleColumns, strategy);
                })
                .switchIfEmpty(Mono.just(obj));
    }

    @SuppressWarnings("unchecked")
    private <T> Mono<T> applyFilter(T obj, Set<String> visibleColumns, MaskStrategy strategy) {
        return Mono.fromCallable(() -> filterViaMap(obj, visibleColumns, strategy))
                .doOnError(e -> log.error("ColumnPermission filter error for {}: {}", obj.getClass().getName(), e.getMessage()))
                .onErrorReturn(obj);
    }

    @SuppressWarnings("unchecked")
    private <T> T filterViaMap(T obj, Set<String> visibleColumns, MaskStrategy strategy) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.convertValue(obj, Map.class);
        if (map == null) {
            return obj;
        }

        Set<String> keys = new HashSet<>(map.keySet());
        for (String key : keys) {
            if (!visibleColumns.contains(key)) {
                switch (strategy) {
                    case MASK -> map.put(key, "***");
                    case NULL -> map.put(key, null);
                    case REMOVE -> map.remove(key);
                }
            }
        }

        return objectMapper.convertValue(map, (Class<T>) obj.getClass());
    }

    @Override
    public Mono<Set<String>> getVisibleColumns(String entityCode) {
        return ReactiveSecurityUtils.getVisibleColumns(entityCode);
    }
}
