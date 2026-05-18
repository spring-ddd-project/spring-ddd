package com.springddd.application.service.permission;

import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.permission.EntityColumnMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ColumnPermissionMetadataService {

    private final ColumnPermissionMetadataProvider metadataProvider;

    public Mono<Map<String, List<String>>> getCurrentUserColumnPermissions() {
        return ReactiveSecurityUtils.getCurrentUser()
                .map(user -> {
                    Map<String, Set<String>> perms = user.getColumnPermissions();
                    if (perms == null) return Collections.<String, List<String>>emptyMap();
                    Map<String, List<String>> result = new HashMap<>();
                    perms.forEach((k, v) -> result.put(k, new ArrayList<>(v)));
                    return result;
                })
                .switchIfEmpty(Mono.just(Collections.emptyMap()));
    }

    public Mono<List<String>> getVisibleColumns(String entityCode) {
        return ReactiveSecurityUtils.getVisibleColumns(entityCode)
                .map(v -> (List<String>) new ArrayList<>(v))
                .switchIfEmpty(Mono.just(Collections.emptyList()));
    }

    public Mono<List<EntityColumnMetadata>> getAllEntityMetadata() {
        return Mono.just(metadataProvider.getAllMetadata());
    }
}
