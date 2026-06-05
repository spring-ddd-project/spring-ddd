package com.springddd.application.service.permission;

import com.springddd.domain.permission.DataPermissionEntity;
import com.springddd.domain.permission.EntityColumnMetadata;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class EntityMetadataScanner {

    private static final String BASE_PACKAGE = "com.springddd.infrastructure.persistence.entity";

    private final Map<String, EntityColumnMetadata> metadataCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        scanEntities();
    }

    private void scanEntities() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));

        Set<BeanDefinition> candidates = scanner.findCandidateComponents(BASE_PACKAGE);

        for (BeanDefinition candidate : candidates) {
            try {
                Class<?> clazz = loadEntityClass(candidate.getBeanClassName());
                Table tableAnnotation = clazz.getAnnotation(Table.class);
                DataPermissionEntity dpAnnotation = clazz.getAnnotation(DataPermissionEntity.class);

                String entityCode = tableAnnotation.value();
                String entityName = (dpAnnotation != null && !dpAnnotation.name().isEmpty())
                        ? dpAnnotation.name()
                        : entityCode;

                List<EntityColumnMetadata.ColumnInfo> columns = extractColumns(clazz);
                EntityColumnMetadata metadata = new EntityColumnMetadata(entityCode, entityName, columns);
                metadataCache.put(entityCode, metadata);
            } catch (ClassNotFoundException e) {
                log.warn("Failed to load entity class: {}", candidate.getBeanClassName(), e);
            }
        }

        log.info("Scanned {} data-permission entities", metadataCache.size());
    }

    Class<?> loadEntityClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    private List<EntityColumnMetadata.ColumnInfo> extractColumns(Class<?> clazz) {
        List<EntityColumnMetadata.ColumnInfo> columns = new ArrayList<>();
        Set<String> fieldNames = new LinkedHashSet<>();

        // Collect all declared fields including inherited ones
        Class<?> current = clazz;
        while (current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                // Skip Spring Data internal fields and static fields
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                String name = field.getName();
                if ("serialVersionUID".equals(name)) {
                    continue;
                }
                if (fieldNames.add(name)) {
                    columns.add(new EntityColumnMetadata.ColumnInfo(name, name));
                }
            }
            current = current.getSuperclass();
        }

        return columns;
    }

    public List<EntityColumnMetadata> getAllMetadata() {
        return new ArrayList<>(metadataCache.values());
    }

    public Optional<EntityColumnMetadata> getMetadata(String entityCode) {
        return Optional.ofNullable(metadataCache.get(entityCode));
    }

    public boolean hasEntity(String entityCode) {
        return metadataCache.containsKey(entityCode);
    }

    public boolean hasField(String entityCode, String fieldName) {
        return metadataCache.get(entityCode) != null
                && metadataCache.get(entityCode).columns().stream()
                        .anyMatch(c -> c.field().equals(fieldName));
    }
}
