package com.springddd.application.service.permission;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DefaultEntityPathResolver implements EntityPathResolver {

    private final Map<String, String> pathMappings = new ConcurrentHashMap<>();

    @Value("#{${spring.ddd.permission.path-mapping:{}}}")
    private Map<String, String> explicitMappings = new HashMap<>();

    @PostConstruct
    public void init() {
        // Register explicit mappings from configuration
        if (explicitMappings != null) {
            pathMappings.putAll(explicitMappings);
        }
        log.info("Registered {} explicit path mappings", pathMappings.size());
    }

    @Override
    public Optional<String> resolveEntityCode(String path) {
        if (path == null || path.isEmpty()) {
            return Optional.empty();
        }

        // 1. Check explicit mappings first
        for (Map.Entry<String, String> entry : pathMappings.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return Optional.of(entry.getValue());
            }
        }

        // 2. Convention-based derivation
        String entityCode = deriveByConvention(path);
        if (entityCode != null && !entityCode.isEmpty()) {
            return Optional.of(entityCode);
        }

        return Optional.empty();
    }

    private String deriveByConvention(String path) {
        // Remove leading slash and convert path segments to snake_case
        String normalized = path.startsWith("/") ? path.substring(1) : path;
        
        // Split by /
        String[] segments = normalized.split("/");
        if (segments.length == 0) {
            return null;
        }

        // Build entity code: e.g., "sys/user" -> "sys_user", "gen/aggregate" -> "gen_aggregate"
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments.length; i++) {
            if (i > 0) {
                sb.append("_");
            }
            sb.append(segments[i]);
        }

        return sb.toString();
    }
}
