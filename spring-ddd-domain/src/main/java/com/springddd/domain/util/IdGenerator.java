package com.springddd.domain.util;

import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.ReactiveSecurityUtils;
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
            return Mono.error(new IllegalStateException("Entity " + entity.getClass().getName() +
                    " has more than one field annotated with both @Id and @SnowflakeId."));
        }

        if (idFields.isEmpty()) {
            return Mono.just(entity);
        }

        Field field = idFields.getFirst();
        field.setAccessible(true);

        try {
            Object currentValue = field.get(entity);
            if (currentValue == null) {
                long id = IdTemp.generateId();
                field.set(entity, id);
                return setAuditFields(entity, createdByFields, lastModifiedByFields, true);
            } else {
                return setAuditFields(entity, List.of(), lastModifiedByFields, false);
            }
        } catch (IllegalAccessException e) {
            return Mono.error(new RuntimeException("Failed to generate ID for field: " + field.getName(), e));
        }
    }

    private Mono<Object> setAuditFields(Object entity, List<Field> createdByFields,
                                         List<Field> lastModifiedByFields, boolean isNew) {
        return ReactiveSecurityUtils.getCurrentUser()
                .map(AuthUser::getUsername)
                .defaultIfEmpty("system")
                .flatMap(username -> {
                    try {
                        if (isNew && !createdByFields.isEmpty()) {
                            Field createdByField = createdByFields.getFirst();
                            createdByField.setAccessible(true);
                            createdByField.set(entity, username);
                        }
                        if (!lastModifiedByFields.isEmpty()) {
                            Field lastModifiedByField = lastModifiedByFields.getFirst();
                            lastModifiedByField.setAccessible(true);
                            lastModifiedByField.set(entity, username);
                        }
                        return Mono.just(entity);
                    } catch (IllegalAccessException e) {
                        return Mono.error(new RuntimeException("Failed to set audit fields", e));
                    }
                })
                .onErrorResume(e -> Mono.just(entity));
    }
}
