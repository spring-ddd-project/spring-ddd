package com.springddd.application.service.permission;

import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.permission.EntityColumnMetadata;
import com.springddd.domain.role.ColumnRule;
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
                    List<ColumnRule> rules = user.getColumnRules();
                    if (rules != null && !rules.isEmpty()) {
                        Map<String, List<String>> result = new HashMap<>();
                        rules.forEach(rule -> {
                            String entityCode = rule.getEntityCode();
                            List<String> columns = rule.getColumns();
                            if (entityCode != null && columns != null) {
                                result.computeIfAbsent(entityCode, k -> new ArrayList<>())
                                        .addAll(columns);
                            }
                        });
                        return result;
                    }
                    // 回退到旧的预计算映射
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
                .<List<String>>map(v -> new ArrayList<>(v))
                .switchIfEmpty(Mono.just(Collections.emptyList()));
    }

    public Mono<List<EntityColumnMetadata>> getAllEntityMetadata() {
        return Mono.just(metadataProvider.getAllMetadata());
    }
}
