package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnTypeNullException;
import com.springddd.domain.gen.exception.ComponentTypeNullException;
import com.springddd.domain.gen.exception.JavaTypeNullException;
import com.springddd.domain.gen.exception.TypeScriptTypeNullException;
import org.springframework.util.ObjectUtils;

public record GenColumnBindBasicInfo(String columnType, String entityType, Byte componentType, Byte typescriptType) {

    public GenColumnBindBasicInfo {
        if (ObjectUtils.isEmpty(columnType)) {
            throw new ColumnTypeNullException();
        }
        if (ObjectUtils.isEmpty(entityType)) {
            throw new JavaTypeNullException();
        }
        if (ObjectUtils.isEmpty(componentType)) {
            throw new ComponentTypeNullException();
        }
        if (ObjectUtils.isEmpty(typescriptType)) {
            throw new TypeScriptTypeNullException();
        }
    }
}
