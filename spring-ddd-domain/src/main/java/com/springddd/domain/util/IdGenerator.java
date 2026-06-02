package com.springddd.domain.util;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.leaf.service.LeafSegmentIdGenerateDomainService;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Component
public class IdGenerator implements BeforeConvertCallback<Object> {

    private final LeafSegmentIdGenerateDomainService leafSegmentIdGenerateDomainService;

    public IdGenerator(LeafSegmentIdGenerateDomainService leafSegmentIdGenerateDomainService) {
        this.leafSegmentIdGenerateDomainService = leafSegmentIdGenerateDomainService;
    }

    @Override
    public @NonNull Mono<Object> onBeforeConvert(@NonNull Object entity, @NonNull SqlIdentifier table) {
        return resolveIdGenerate(entity).flatMap(strategy -> {
            boolean classLevelIdGenerate = entity.getClass().isAnnotationPresent(IdGenerate.class);

            List<Field> idFields = Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class) && (classLevelIdGenerate || f.isAnnotationPresent(IdGenerate.class)))
                    .toList();

            List<Field> createdByFields = Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(CreatedBy.class))
                    .toList();

            List<Field> lastModifiedByFields = Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(LastModifiedBy.class))
                    .toList();

            if (idFields.size() > 1) {
                return Mono.error(new IllegalStateException("Entity " + entity.getClass().getName() +
                        " has more than one field annotated with @Id."));
            }

            if (idFields.isEmpty()) {
                return Mono.just(entity);
            }

            Field field = idFields.getFirst();
            field.setAccessible(true);

            Field createdByField = !createdByFields.isEmpty() ? createdByFields.getFirst() : null;
            if (createdByField != null) {
                createdByField.setAccessible(true);
            }

            Field lastModifiedByField = !lastModifiedByFields.isEmpty() ? lastModifiedByFields.getFirst() : null;
            if (lastModifiedByField != null) {
                lastModifiedByField.setAccessible(true);
            }

            Object currentValue = getFieldValue(field, entity);
            if (currentValue == null) {
                return generateId(strategy, entity).map(id -> {
                    setFieldValue(field, entity, id);
                    if (createdByField != null) {
                        setFieldValue(createdByField, entity, SecurityUtils.getUsername());
                    }
                    if (lastModifiedByField != null) {
                        setFieldValue(lastModifiedByField, entity, SecurityUtils.getUsername());
                    }
                    return entity;
                });
            } else {
                if (lastModifiedByField != null) {
                    setFieldValue(lastModifiedByField, entity, SecurityUtils.getUsername());
                }
                return Mono.just(entity);
            }
        }).switchIfEmpty(Mono.just(entity));
    }

    private Mono<IdGenerateStrategy> resolveIdGenerate(Object entity) {
        // Class-level annotation
        IdGenerate classAnno = entity.getClass().getAnnotation(IdGenerate.class);
        if (classAnno != null) {
            return Mono.just(classAnno.strategy());
        }

        // Field-level annotation
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(IdGenerate.class)) {
                IdGenerate fieldAnno = field.getAnnotation(IdGenerate.class);
                return Mono.just(fieldAnno.strategy());
            }
        }

        return Mono.empty();
    }

    private Mono<Long> generateId(IdGenerateStrategy strategy, Object entity) {
        if (strategy == IdGenerateStrategy.LEAF_SEGMENT) {
            String key = resolveKey(entity);
            if (key.isEmpty()) {
                return Mono.error(new IllegalStateException("IdGenerate strategy LEAF_SEGMENT requires a non-empty key."));
            }
            return leafSegmentIdGenerateDomainService.nextId(key);
        }
        return Mono.just(IdTemp.generateId());
    }

    private String resolveKey(Object entity) {
        IdGenerate classAnno = entity.getClass().getAnnotation(IdGenerate.class);
        if (classAnno != null && !classAnno.key().isEmpty()) {
            return classAnno.key();
        }

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(IdGenerate.class)) {
                IdGenerate fieldAnno = field.getAnnotation(IdGenerate.class);
                if (!fieldAnno.key().isEmpty()) {
                    return fieldAnno.key();
                }
            }
        }

        // Auto derive from class name: SysUserEntity -> sys_user
        String className = entity.getClass().getSimpleName();
        if (className.endsWith("Entity")) {
            className = className.substring(0, className.length() - 6);
        }
        return camelToSnake(className);
    }

    private static String camelToSnake(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_').append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    Object getFieldValue(Field field, Object entity) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field: " + field.getName(), e);
        }
    }

    void setFieldValue(Field field, Object entity, Object value) {
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field: " + field.getName(), e);
        }
    }
}
