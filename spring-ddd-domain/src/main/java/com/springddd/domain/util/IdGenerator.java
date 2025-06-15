package com.springddd.domain.util;

import org.springframework.data.annotation.Id;
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

        if (idFields.size() > 1) {
            throw new IllegalStateException("Entity " + entity.getClass().getName() +
                    " has more than one field annotated with both @Id and @SnowflakeId.");
        }

        if (idFields.size() == 1) {
            Field field = idFields.getFirst();
            field.setAccessible(true);
            try {
                Object currentValue = field.get(entity);
                if (currentValue == null) {
                    // 根据自定义 ID 生成器生成 ID
                    long id = System.currentTimeMillis();
                    field.set(entity, id);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to generate ID for field: " + field.getName(), e);
            }
        }

        return Mono.just(entity);
    }
}