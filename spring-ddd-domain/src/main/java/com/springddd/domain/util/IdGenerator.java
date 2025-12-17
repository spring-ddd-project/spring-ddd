package com.springddd.domain.util;

import com.springddd.domain.auth.SecurityUtils;
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

    @Override
    public @NonNull Mono<Object> onBeforeConvert(@NonNull Object entity, @NonNull SqlIdentifier table) {
        List<Field> idFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class) && f.isAnnotationPresent(IdGenerate.class))
                .toList();

        List<Field> createdByFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(CreatedBy.class))
                .toList();

        List<Field> lastModifiedByFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(LastModifiedBy.class))
                .toList();

        if (idFields.size() > 1) {
            throw new IllegalStateException("Entity " + entity.getClass().getName() +
                    " has more than one field annotated with both @Id and @SnowflakeId.");
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
                long id = IdTemp.generateId();
                field.set(entity, id);
                if (createdByField != null) {
                    createdByField.set(entity, SecurityUtils.getUsername());
                }
                if (lastModifiedByField != null) {
                    lastModifiedByField.set(entity, SecurityUtils.getUsername());
                }
            } else {
                if (lastModifiedByField != null) {
                    lastModifiedByField.set(entity, SecurityUtils.getUsername());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to generate ID for field: " + field.getName(), e);
        }

        return Mono.just(entity);
    }
}










