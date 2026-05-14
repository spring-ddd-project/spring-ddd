package com.springddd.domain.util;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.leaf.LeafSegmentDomainService;
import com.springddd.domain.leaf.SnowflakeDomainService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class IdGenerator implements BeforeConvertCallback<Object> {

    private final LeafSegmentDomainService leafSegmentDomainService;
    private final SnowflakeDomainService snowflakeDomainService;

    @Override
    public @NonNull Mono<Object> onBeforeConvert(@NonNull Object entity, @NonNull SqlIdentifier table) {
        List<Field> idFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class) && f.isAnnotationPresent(LeafId.class))
                .toList();

        List<Field> createdByFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(CreatedBy.class))
                .toList();

        List<Field> lastModifiedByFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(LastModifiedBy.class))
                .toList();

        if (idFields.size() > 1) {
            throw new IllegalStateException("Entity " + entity.getClass().getName() +
                    " has more than one field annotated with both @Id and @LeafId.");
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

        try {
            Object currentValue = field.get(entity);
            if (currentValue == null) {
                LeafId idGenerate = field.getAnnotation(LeafId.class);
                String bizTag = idGenerate != null ? idGenerate.value() : "";

                if (!bizTag.isEmpty()) {
                    // Segment mode: use bizTag-based ID allocation
                    return leafSegmentDomainService.getId(bizTag)
                            .doOnNext(id -> {
                                try {
                                    field.set(entity, id);
                                    setAuditFields(entity, createdByField, lastModifiedByField);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException("Failed to set ID for field: " + field.getName(), e);
                                }
                            })
                            .then(Mono.just(entity));
                }

                // Snowflake mode: use distributed snowflake ID
                return snowflakeDomainService.getId()
                        .doOnNext(id -> {
                            try {
                                field.set(entity, id);
                                setAuditFields(entity, createdByField, lastModifiedByField);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Failed to set ID for field: " + field.getName(), e);
                            }
                        })
                        .then(Mono.just(entity));
            } else {
                setLastModifiedBy(entity, lastModifiedByField);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to generate ID for field: " + field.getName(), e);
        }

        return Mono.just(entity);
    }

    private void setAuditFields(Object entity, Field createdByField, Field lastModifiedByField) throws IllegalAccessException {
        if (createdByField != null) {
            createdByField.set(entity, SecurityUtils.getUsername());
        }
        if (lastModifiedByField != null) {
            lastModifiedByField.set(entity, SecurityUtils.getUsername());
        }
    }

    private void setLastModifiedBy(Object entity, Field lastModifiedByField) throws IllegalAccessException {
        if (lastModifiedByField != null) {
            lastModifiedByField.set(entity, SecurityUtils.getUsername());
        }
    }
}
